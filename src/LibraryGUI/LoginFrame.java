package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JTextField txtUser = new JTextField(15);
	private final JPasswordField txtPass = new JPasswordField(15);

	public LoginFrame() {
		setTitle("Library Login");
		setSize(420, 260);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel header = new JPanel();
		header.setBackground(new Color(60, 130, 220));
		header.setPreferredSize(new Dimension(400, 90));
		JLabel title = new JLabel("Library Management System");
		title.setForeground(Color.WHITE);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		header.add(title);
		add(header, BorderLayout.NORTH);

		JPanel center = new JPanel(new GridBagLayout());
		center.setBackground(new Color(230, 240, 255));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
		center.add(new JLabel("Username:"), gbc);
		gbc.gridy++;
		center.add(new JLabel("Password:"), gbc);

		gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		center.add(txtUser, gbc);
		gbc.gridy++;
		center.add(txtPass, gbc);

		add(center, BorderLayout.CENTER);

		JButton btnLogin = new JButton("Login");
		btnLogin.setPreferredSize(new Dimension(100, 30));
		JPanel bottom = new JPanel();
		bottom.add(btnLogin);
		add(bottom, BorderLayout.SOUTH);

		btnLogin.addActionListener(e -> doLogin());
	}

	private void doLogin() {
		String username = txtUser.getText().trim();
		String password = new String(txtPass.getPassword());
		
		User matched = null;
			for (User u : LibraryData.USERS) {
				if (u.getUsername().equals(username) &&
					u.getPassword().equals(password)) {
					matched = u;
					break;
				}
			}

			if (matched == null) {
				JOptionPane.showMessageDialog(this,
						"Invalid username or password",
						"Login failed",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			LibrarySystemDashboard dash = new LibrarySystemDashboard(matched);
			dash.setVisible(true);
			dispose();
	}
}

