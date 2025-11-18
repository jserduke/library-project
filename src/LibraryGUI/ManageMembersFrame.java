package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageMembersFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"ID","F-Name","L-Name","Phone","Email","Gender"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { return false; }
	};
	private JTable table = new JTable(model);

	public ManageMembersFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Manage Members", true);
		setSize(750, 400);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 153, 102));
		JLabel lbl = new JLabel("  Manage Members");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		JButton btnAdd    = new JButton("Add");
		JButton btnEdit   = new JButton("Edit");
		JButton btnDelete = new JButton("Delete");
		buttons.add(btnAdd);
		buttons.add(btnEdit);
		buttons.add(btnDelete);
		add(buttons, BorderLayout.SOUTH);

		btnAdd.addActionListener(e -> new AddMemberFrame(dashboard).setVisible(true));
		btnEdit.addActionListener(e -> editSelected());
		btnDelete.addActionListener(e -> deleteSelected());

		reload();
	}

	private void reload() {
		model.setRowCount(0);
		for (Member m : LibraryData.MEMBERS) {
			model.addRow(new Object[]{
					m.getId(), m.getFirstName(), m.getLastName(),
					m.getPhone(), m.getEmail(), m.getGender()
			});
		}
	}

	private void editSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a member first");
			return;
		}
		int id = (int) model.getValueAt(row, 0);
		EditMemberFrame f = new EditMemberFrame((LibrarySystemDashboard) getParent());
		f.setVisible(true);
		reload();
	}

	private void deleteSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a member first");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this member?", "Confirm",
				JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) 
			return;

		int id = (int) model.getValueAt(row, 0);
		LibraryData.MEMBERS.removeIf(m -> m.getId() == id);
		reload();
	}
}

