package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class AddMemberFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtFirst   = new JTextField(15);
	private JTextField txtLast    = new JTextField(15);
	private JTextField txtPhone   = new JTextField(15);
	private JTextField txtEmail   = new JTextField(15);
	private JComboBox<String> cbGender =
			new JComboBox<>(new String[]{"Male", "Female"});

	private LibrarySystemDashboard dashboard;

	public AddMemberFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Add Member", true);
		this.dashboard = dashboard;

		setSize(400, 350);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 153, 102));
		JLabel lbl = new JLabel("  Add Member");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.LINE_END;

		int row = 0;
		gbc.gridx = 0; 
		gbc.gridy = row; 
		form.add(new JLabel("First Name:"), gbc);
		gbc.gridy++; form.add(new JLabel("Last Name:"), gbc);
		gbc.gridy++; form.add(new JLabel("Phone:"), gbc);
		gbc.gridy++; form.add(new JLabel("Email:"), gbc);
		gbc.gridy++; form.add(new JLabel("Gender:"), gbc);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 1;
		gbc.gridy = 0;
		form.add(txtFirst, gbc);
		gbc.gridy++; form.add(txtLast, gbc);
		gbc.gridy++; form.add(txtPhone, gbc);
		gbc.gridy++; form.add(txtEmail, gbc);
		gbc.gridy++; form.add(cbGender, gbc);

		JButton btnAdd = new JButton("Add New Member");
		btnAdd.addActionListener(e -> addMember());

		gbc.gridx = 0; 
		gbc.gridy++; 
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		form.add(btnAdd, gbc);

		add(form, BorderLayout.CENTER);
	}

	private void addMember() {
		int id = LibraryData.nextMemberId();
		Member m = new Member(
				id,
				txtFirst.getText().trim(),
				txtLast.getText().trim(),
				txtPhone.getText().trim(),
				txtEmail.getText().trim(),
				(String) cbGender.getSelectedItem()
				);
		LibraryData.MEMBERS.add(m);
		JOptionPane.showMessageDialog(this, "Member added with ID: " + id);
		dashboard.refreshDashboardCounts();
		dispose();
	}
}
