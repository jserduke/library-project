package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MembersListFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"ID","F-Name","L-Name","Phone","Email","Gender"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { return false; }
	};
	private JTable table = new JTable(model);

	public MembersListFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Members List", true);
		setSize(700, 350);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 153, 102));
		JLabel lbl = new JLabel("  Members List");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		add(new JScrollPane(table), BorderLayout.CENTER);

		JButton btnInfo = new JButton("Show Member Info Card");
		btnInfo.addActionListener(e -> showInfo());
		JPanel bottom = new JPanel();
		bottom.add(btnInfo);
		add(bottom, BorderLayout.SOUTH);

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

	private void showInfo() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a member first");
			return;
		}	
		int id = (int) model.getValueAt(row, 0);
		for (Member m : LibraryData.MEMBERS) {
			if (m.getId() == id) {
				new MemberInfoCardFrame(this, m).setVisible(true);
				break;
			}
		}
	}
}

