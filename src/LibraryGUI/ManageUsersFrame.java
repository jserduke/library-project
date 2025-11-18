package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageUsersFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"Username","Role"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { 
			return false; 
		}
	};
	private JTable table = new JTable(model);

	private JTextField txtUser = new JTextField(10);
	private JPasswordField txtPass = new JPasswordField(10);
	private JComboBox<User.Role> cbRole =
			new JComboBox<>(new User.Role[]{User.Role.USER, User.Role.ADMIN});

	public ManageUsersFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Manage Users", true);
		setSize(600, 320);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(60, 120, 200));
		JLabel lbl = new JLabel("  Manage Users");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Username:"));
		bottom.add(txtUser);
		bottom.add(new JLabel("Password:"));
		bottom.add(txtPass);
		bottom.add(new JLabel("Role:"));
		bottom.add(cbRole);

		JButton btnAdd = new JButton("Add User");
		JButton btnDelete = new JButton("Delete User");
		bottom.add(btnAdd);
		bottom.add(btnDelete);

		add(bottom, BorderLayout.SOUTH);

		btnAdd.addActionListener(e -> addUser());
		btnDelete.addActionListener(e -> deleteUser());

		reload();
	}

	private void reload() {
		model.setRowCount(0);
		for (User u : LibraryData.USERS) {
			model.addRow(new Object[]{u.getUsername(), u.getRole().name()});
		}
	}

	private void addUser() {
		String username = txtUser.getText().trim();
		String password = new String(txtPass.getPassword());
		if (username.isEmpty() || password.isEmpty()) 
			return;

		LibraryData.USERS.add(new User(username, password,
             (User.Role) cbRole.getSelectedItem()));
		txtUser.setText("");
		txtPass.setText("");
		reload();
	}

	private void deleteUser() {
		int row = table.getSelectedRow();
		if (row < 0) 
			return;
		String username = (String) model.getValueAt(row, 0);
		LibraryData.USERS.removeIf(u -> u.getUsername().equals(username));
		reload();
	}
}
