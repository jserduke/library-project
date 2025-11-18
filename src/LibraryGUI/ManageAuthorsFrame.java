package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageAuthorsFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"ID","Name"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { return false; }
	};
	private JTable table = new JTable(model);
	private JTextField txtName = new JTextField(15);

	public ManageAuthorsFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Manage Authors", true);
		setSize(500, 300);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(120, 80, 180));
		JLabel lbl = new JLabel("  Manage Authors");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Name:"));
		bottom.add(txtName);
		JButton btnAdd    = new JButton("Add");
		JButton btnDelete = new JButton("Delete");
		bottom.add(btnAdd);
		bottom.add(btnDelete);
		add(bottom, BorderLayout.SOUTH);

		btnAdd.addActionListener(e -> addAuthor());
		btnDelete.addActionListener(e -> deleteAuthor());

		reload();
	}

	private void reload() {
		model.setRowCount(0);
		int id = 1;
		for (String a : LibraryData.AUTHORS) {
			model.addRow(new Object[]{id++, a});
		}
	}

	private void addAuthor() {
		String name = txtName.getText().trim();
		if (name.isEmpty()) 
			return;
		LibraryData.AUTHORS.add(name);
		txtName.setText("");
		reload();
	}

	private void deleteAuthor() {
		int row = table.getSelectedRow();
		if (row < 0) 
			return;
		String name = (String) model.getValueAt(row, 1);
		LibraryData.AUTHORS.remove(name);
		reload();
	}
}
