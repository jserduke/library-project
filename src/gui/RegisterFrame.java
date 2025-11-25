package gui;

import javax.swing.*;

import client.ResponseHandler;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import message.*;

public class RegisterFrame extends JDialog {
    private static final long serialVersionUID = 1L;
	private JTextField txtFirst = new JTextField(12);
    private JTextField txtLast  = new JTextField(12);
    private JTextField txtBirth = new JTextField(10); // YYYY-MM-DD
    // private JTextField txtPhone = new JTextField(12);
    private JTextField txtEmail = new JTextField(16);
    private JPasswordField txtPass = new JPasswordField(12);

    public RegisterFrame(Window parent, ObjectOutputStream requestWriter, ResponseHandler responseHandler, boolean isFromAdmin) {
        super(parent, "Create " + (isFromAdmin ? "Admin" : "Member") + " Account", ModalityType.APPLICATION_MODAL);
        setSize(420, 300);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.LINE_END;

        gbc.gridx=0; 
        gbc.gridy=0; 
        form.add(new JLabel("First name:"), gbc);
        gbc.gridy++; 
        form.add(new JLabel("Last name:"), gbc);
        gbc.gridy++; 
        form.add(new JLabel("Birthday (YYYY-MM-DD):"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Email (username):"), gbc);
        gbc.gridy++; 
        form.add(new JLabel("Password:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx=1; 
        gbc.gridy=0; 
        form.add(txtFirst, gbc);
        gbc.gridy++; 
        form.add(txtLast, gbc);
        gbc.gridy++; 
        form.add(txtBirth, gbc);
        gbc.gridy++;
        form.add(txtEmail, gbc);
        gbc.gridy++;
        form.add(txtPass, gbc);

        JButton btnCreate = new JButton("Create Account");
        JButton btnCancel = new JButton("Cancel");
        JPanel btns = new JPanel();
        btns.add(btnCancel); 
        btns.add(btnCreate);

        add(form, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);

        btnCreate.addActionListener(e -> create(requestWriter, responseHandler, isFromAdmin));
        btnCancel.addActionListener(e -> dispose());
    }

    private void create(ObjectOutputStream requestWriter, ResponseHandler responseHandler, boolean isFromAdmin) {
        try {
            String first = txtFirst.getText().trim();
            String last  = txtLast.getText().trim();
            String dobString = txtBirth.getText().trim();
            String[] dobParts = dobString.split("-");
            Date dob = new Date(Integer.parseInt(dobParts[0]) - 1900, Integer.parseInt(dobParts[1]) - 1, Integer.parseInt(dobParts[2]));
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPass.getPassword());

            if (first.isEmpty() || last.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }
            
            ArrayList<String> info = new ArrayList<String>();
            Message registrationMessage = new Message(0, message.Type.REQUEST, -1, message.Action.REGISTER, Status.PENDING, info);
            info.add(email);
            info.add(pass);
            info.add(first + " " + last);
            info.add(dob.toString());
            info.add(Boolean.toString(isFromAdmin));
            responseHandler.setRequestIdExpected(registrationMessage.getId());
            responseHandler.setOldDialog(this);
            try {
    			requestWriter.writeObject(registrationMessage);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            /*
            if (LibraryData.findUser(email, pass) != null) {
                JOptionPane.showMessageDialog(this, "User already exists.");
                return;
            }
            int id = LibraryData.nextMemberId();
            Member m = new Member(id, first, last, dob, "", email);
            LibraryData.MEMBERS.add(m);
            User u = new User(email, pass, User.Role.USER);
            u.setMemberId(id);
            LibraryData.USERS.add(u);
            JOptionPane.showMessageDialog(this, "Account created. You can now log in.");
            */
            // dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage());
        }
    }
}
