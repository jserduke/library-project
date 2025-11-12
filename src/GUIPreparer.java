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
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.LOGIN, Status.PENDING, "Please let me in");
				// System.out.println(messageToServer.getId());
				responseHandler.setFrame(frame);
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		JButton registerButton = new JButton("Register");
		panel.add(titleField);
		panel.add(loginButton);
		panel.add(registerButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(560, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// panel.add(new JButton("will this work?"));
	}
	
	public void updateHomePageToLoggedIn(JFrame frame, Message response) {
		JPanel panel = (JPanel) frame.getContentPane().getComponents()[0];
		System.out.println("# of components: " + panel.getComponents().length);
		JTextField titleField = (JTextField) panel.getComponents()[0];
		titleField.setText("Home Page for " + response.getInfo());
		panel.remove(1);
		panel.remove(1);
		JButton getCheckoutsButton = new JButton("Get checkouts");
		getCheckoutsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.GET_CHECKOUTS, Status.PENDING, "Give me records");
				// System.out.println(messageToServer.getId());
				responseHandler.setFrame(null);
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		JButton checkoutButton = new JButton("Checkout the book");
		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Message request = new Message(0, Type.REQUEST, -1, Action.CHECKOUT, Status.PENDING, "Checkout please!");
				responseHandler.setFrame(frame);
				responseHandler.setRequestIdExpected(request.getId());
				try {
					requestWriter.writeObject(request);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		panel.add(getCheckoutsButton);
		panel.add(checkoutButton);
		panel.revalidate();
	}
	
	public void showCheckoutHistory(JFrame frame, Message response) {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		LayoutManager layout = new GridLayout(0, 1);
		panel.setLayout(layout);
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
