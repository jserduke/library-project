package LibraryGUI;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageGenresFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = new DefaultTableModel(
			new Object[]{"ID","Name"}, 0) {
		@Override public boolean isCellEditable(int r, int c) { return false; }
	};
	private JTable table = new JTable(model);
	private JTextField txtName = new JTextField(15);

	public ManageGenresFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Manage Genres", true);
		setSize(500, 300);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 180, 170));
		JLabel lbl = new JLabel("  Manage Book Genres");
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
     
     	btnAdd.addActionListener(e -> addGenre());
     	btnDelete.addActionListener(e -> deleteGenre());

     	reload();
	}

	private void reload() {
		model.setRowCount(0);
		int id = 1;
		for (String g : LibraryData.GENRES) {
			model.addRow(new Object[]{id++, g});
		}
	}

	private void addGenre() {
		String name = txtName.getText().trim();
		if (name.isEmpty()) 
			return;
		LibraryData.GENRES.add(name);
		txtName.setText("");
		reload();
	}

	private void deleteGenre() {
		int row = table.getSelectedRow();
		if (row < 0)
			return;
		String name = (String) model.getValueAt(row, 1);
		LibraryData.GENRES.remove(name);
		reload();
	}
}
