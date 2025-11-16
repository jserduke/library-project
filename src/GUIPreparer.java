import java.io.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUIPreparer {
	private ObjectOutputStream requestWriter;
	private ResponseHandler responseHandler;
	
	public GUIPreparer(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
		this.requestWriter = requestWriter;
		this.responseHandler = responseHandler;
	}
	
	public void createHomePageNotLoggedIn(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		panel.setLayout(layout);
		
		JTextField titleField = new JTextField("HOME PAGE: LOG IN TO CHECKOUT");
		titleField.setEditable(false);
		JButton loginButton = new JButton("Login");
		// SEND LOGIN REQUEST WHEN BUTTON IS CLICKED
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.LOGIN, Status.PENDING, "Please let me in");
				// FRAME (WINDOW) TO BE EDITED WHEN RESPONSE FROM SERVER IS RECEIVED
				responseHandler.setFrame(frame);
				// ID CORRESPONDING TO LOGIN REQUEST THAT RESPONSE SHOULD BE LOOKING FOR
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		// NOT FUNCTIONAL YET
		JButton registerButton = new JButton("Register");
		panel.add(titleField);
		panel.add(loginButton);
		panel.add(registerButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(560, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void updateHomePageToLoggedIn(JFrame frame, Message response) {
		// GET PANE IN WHICH BUTTONS EXIST
		JPanel panel = (JPanel) frame.getContentPane().getComponents()[0];
		// System.out.println("# of components: " + panel.getComponents().length);
		JTextField titleField = (JTextField) panel.getComponents()[0];
		// UPDATE HOME PAGE TEXT AFTER SUCCESSFUL LOGIN
		titleField.setText("Home Page for " + response.getInfo());
		// REMOVE LOGIN AND REGISTRATION BUTTONS
		panel.remove(1);
		panel.remove(1);
		// ADD BUTTON TO GET CHECKOUT HISTORY
		JButton getCheckoutsButton = new JButton("Get checkouts");
		getCheckoutsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.GET_CHECKOUTS, Status.PENDING, "Give me records");
				// NEW WINDOW WILL BE CREATED
				responseHandler.setFrame(null);
				// INFORM ResponseHandler OF WHICH CORRESPONDING RESPONSE THEY SHOULD BE EXPECTING NEXT
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		// ADD BUTTON TO CHECKOUT A BOOK
		JButton checkoutButton = new JButton("Checkout the book");
		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.CHECKOUT, Status.PENDING, "Checkout please!");
				// JOptionPane WILL BE ATTACHED TO HOME PAGE WINDOW
				responseHandler.setFrame(frame);
				// INFORM ResponseHandler OF WHICH CORRESPONDING RESPONSE THEY SHOULD BE EXPECTING NEXT
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		// ADD THE NEW BUTTONS
		panel.add(getCheckoutsButton);
		panel.add(checkoutButton);
		// RERENDER WINDOW WITH NEW BUTTONS (ALSO MIGHT BE ABLE TO JUST MAKE INVISIBLE AND THEN MAKE VISIBLE AGAIN)
		panel.revalidate();
	}
	
	public void showCheckoutHistory(JFrame frame, Message response) {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		LayoutManager layout = new GridLayout(0, 1);
		panel.setLayout(layout);
		// DISPLAY GRID OF CHECKOUT HISTORY
		System.out.println(response.getInfo());
		for (int i = 0; i < response.getInfo().length(); i += 1) {
			String recordText = Character.toString(response.getInfo().charAt(i));
			JTextField checkoutRecord = new JTextField(recordText);
			checkoutRecord.setEditable(false);
			panel.add(checkoutRecord);
		}
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
