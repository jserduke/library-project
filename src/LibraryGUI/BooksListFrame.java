package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


	public class BooksListFrame extends JDialog {

		private static final long serialVersionUID = 1L;
		private DefaultTableModel model = new DefaultTableModel(
				new Object[]{"ID","ISBN","Title","Author","Genre","Qty","Available"}, 0) {
			@Override public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		private JTable table = new JTable(model);

		public BooksListFrame(LibrarySystemDashboard dashboard) {
			super(dashboard, "Books List", true);
			setSize(800, 350);
			setLocationRelativeTo(dashboard);
			setLayout(new BorderLayout());

			JPanel header = new JPanel(new BorderLayout());
			header.setBackground(new Color(204, 102, 0));
			JLabel lbl = new JLabel("  Books List");
			lbl.setForeground(Color.WHITE);
			lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
			header.add(lbl, BorderLayout.WEST);
			add(header, BorderLayout.NORTH);

			add(new JScrollPane(table), BorderLayout.CENTER);

			JButton btnInfo = new JButton("Show Book Info Card");
			btnInfo.addActionListener(e -> showInfo());
			JPanel bottom = new JPanel();
			bottom.add(btnInfo);
			add(bottom, BorderLayout.SOUTH);

			reload();
		}

		private void reload() {
			model.setRowCount(0);
			for (Book b : LibraryData.BOOKS) {
				model.addRow(new Object[]{
						b.getId(), b.getIsbn(), b.getTitle(), b.getAuthor(),
						b.getGenre(), b.getQuantity(), b.isAvailable() ? "YES" : "NO"
				});
			}
		}

		private void showInfo() {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "Select a book first");
				return;
			}
			int id = (int) model.getValueAt(row, 0);
			for (Book b : LibraryData.BOOKS) {
				if (b.getId() == id) {
					new BookInfoCardFrame(this, b).setVisible(true);
					break;
				}
			}
		}
}

