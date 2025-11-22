package gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
	private final JTextField txtUser = new JTextField(18);
    private final JPasswordField txtPass = new JPasswordField(18);

    public LoginFrame() {
        setTitle("Library Login");
        setSize(460, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("  Sign in");
        Theme.styleHeaderBar(header, title);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // FORM
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.SURFACE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx=0; 
        gbc.gridy=0; 
        center.add(new JLabel("Email:"), gbc);
        gbc.gridy++; 
        center.add(new JLabel("Password:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx=1; 
        gbc.gridy=0; gbc.fill=GridBagConstraints.HORIZONTAL; 
        center.add(txtUser, gbc);
        gbc.gridy++; 
        center.add(txtPass, gbc);
        add(center, BorderLayout.CENTER);

        // BUTTONS
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Create an Account");
        Theme.styleButton(btnRegister, Theme.ACCENT_GREEN);
        Theme.styleButton(btnLogin, Theme.ACCENT_BLUE);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(Theme.SURFACE);
        btns.add(btnRegister); btns.add(btnLogin);
        add(btns, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> new RegisterFrame(this).setVisible(true));
    }

    private void doLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        User found = LibraryData.findUser(u, p);
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Invalid username/password", "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (found.getRole() == User.Role.ADMIN) {
            new AdminPortalFrame(found).setVisible(true);
        } else {
            new MemberPortalFrame(found).setVisible(true);
        }
        dispose();
    }
}
