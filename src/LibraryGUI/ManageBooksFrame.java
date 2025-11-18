package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageBooksFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"ID","Title","Author","Genre","Qty","Available"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { 
			return false;
		}
	};
	private JTable table = new JTable(model);

	public ManageBooksFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Manage Books", true);
		setSize(800, 400);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(204, 102, 0));
    	JLabel lbl = new JLabel("  Manage Books");
    	lbl.setForeground(Color.WHITE);
     	lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
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

     	btnAdd.addActionListener(e -> new AddBookFrame(dashboard).setVisible(true));
     	btnEdit.addActionListener(e -> new EditBookFrame(dashboard).setVisible(true));
     	btnDelete.addActionListener(e -> deleteSelected());

     	reload();
	}

	private void reload() {
		model.setRowCount(0);
		for (Book b : LibraryData.BOOKS) {
			model.addRow(new Object[]{
					b.getId(), b.getTitle(), b.getAuthor(),
					b.getGenre(), b.getQuantity(),
					b.isAvailable() ? "YES" : "NO"
			});
		}
	}

	private void deleteSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a book first");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this book?", "Confirm",
				JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) 
			return;

		int id = (int) model.getValueAt(row, 0);
		LibraryData.BOOKS.removeIf(b -> b.getId() == id);
		reload();
	}
}

