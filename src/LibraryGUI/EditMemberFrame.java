package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class EditMemberFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtId      = new JTextField(5);
	private JTextField txtFirst   = new JTextField(15);
	private JTextField txtLast    = new JTextField(15);
	private JTextField txtPhone   = new JTextField(15);
	private JTextField txtEmail   = new JTextField(15);
	private JComboBox<String> cbGender =
			new JComboBox<>(new String[]{"Male", "Female"});

	private Member current;

	public EditMemberFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Edit Member", true);
		setSize(420, 400);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 153, 102));
		JLabel lbl = new JLabel("  Edit Member");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.LINE_END;

		int row = 0;
		gbc.gridx = 0; gbc.gridy = row; form.add(new JLabel("Enter Member ID:"), gbc);
		gbc.gridy++; form.add(new JLabel("First Name:"), gbc);
		gbc.gridy++; form.add(new JLabel("Last Name:"), gbc);
		gbc.gridy++; form.add(new JLabel("Phone:"), gbc);
		gbc.gridy++; form.add(new JLabel("Email:"), gbc);
		gbc.gridy++; form.add(new JLabel("Gender:"), gbc);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 1; gbc.gridy = 0;
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    	txtId.setColumns(6);
    	JButton btnSearch = new JButton("search");
    	idPanel.add(txtId);
    	idPanel.add(btnSearch);
    	form.add(idPanel, gbc);

    	gbc.gridy++; form.add(txtFirst, gbc);
    	gbc.gridy++; form.add(txtLast, gbc);
    	gbc.gridy++; form.add(txtPhone, gbc);
    	gbc.gridy++; form.add(txtEmail, gbc);
    	gbc.gridy++; form.add(cbGender, gbc);

    	JButton btnSave = new JButton("Edit Member Info");
    	gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
    	form.add(btnSave, gbc);

    	add(form, BorderLayout.CENTER);
     
    	btnSearch.addActionListener(e -> searchMember());
    	btnSave.addActionListener(e -> saveMember());
	}

	private void searchMember() {
		int id;
		try {
			id = Integer.parseInt(txtId.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid ID");
			return;
		}

		current = null;
		for (Member m : LibraryData.MEMBERS) {
			if (m.getId() == id) { current = m; break; }
		}
		if (current == null) {
			JOptionPane.showMessageDialog(this, "Member not found");
			return;
		}

		txtFirst.setText(current.getFirstName());
		txtLast.setText(current.getLastName());
		txtPhone.setText(current.getPhone());
		txtEmail.setText(current.getEmail());
		cbGender.setSelectedItem(current.getGender());
	}

	private void saveMember() {
		if (current == null) {
			JOptionPane.showMessageDialog(this, "Search a member first");
			return;
		}
		current.setFirstName(txtFirst.getText().trim());
		current.setLastName(txtLast.getText().trim());
		current.setPhone(txtPhone.getText().trim());
		current.setEmail(txtEmail.getText().trim());
		current.setGender((String) cbGender.getSelectedItem());
		JOptionPane.showMessageDialog(this, "Member updated");
		dispose();
	}
}
