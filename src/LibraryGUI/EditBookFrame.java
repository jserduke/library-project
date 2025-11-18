package LibraryGUI;

import javax.swing.*;
import java.awt.*;


public class EditBookFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtId     = new JTextField(5);
	private JTextField txtIsbn   = new JTextField(10);
	private JTextField txtTitle  = new JTextField(20);
	private JTextField txtAuthor = new JTextField(15);
	private JTextField txtGenre  = new JTextField(10);
	private JSpinner spQuantity  =
			new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

	private Book current;

	public EditBookFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Edit Book", true);
		setSize(550, 320);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(204, 102, 0));
		JLabel lbl = new JLabel("  Edit Book");
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
		form.add(new JLabel("Enter Book ID:"), gbc);
		gbc.gridy++; form.add(new JLabel("ISBN:"), gbc);
		gbc.gridy++; form.add(new JLabel("Name:"), gbc);
		gbc.gridy++; form.add(new JLabel("Author:"), gbc);
		gbc.gridy++; form.add(new JLabel("Genre:"), gbc);
		gbc.gridy++; form.add(new JLabel("Quantity:"), gbc);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 1; gbc.gridy = 0;
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
		JButton btnSearch = new JButton("search");
		txtId.setColumns(6);
		idPanel.add(txtId);
		idPanel.add(btnSearch);
		form.add(idPanel, gbc);

		gbc.gridy++; form.add(txtIsbn, gbc);
		gbc.gridy++; form.add(txtTitle, gbc);
		gbc.gridy++; form.add(txtAuthor, gbc);
     	gbc.gridy++; form.add(txtGenre, gbc);
     	gbc.gridy++; form.add(spQuantity, gbc);

     	JButton btnSave = new JButton("Save Book");
     	gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
     	gbc.anchor = GridBagConstraints.CENTER;
     	form.add(btnSave, gbc);

     	add(form, BorderLayout.CENTER);

     	btnSearch.addActionListener(e -> searchBook());
     	btnSave.addActionListener(e -> saveBook());
	}

	private void searchBook() {
		int id;
		try {
			id = Integer.parseInt(txtId.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid ID");
			return;
		}
		current = null;
		for (Book b : LibraryData.BOOKS) {
			if (b.getId() == id) { current = b; break; }
		}
		if (current == null) {
			JOptionPane.showMessageDialog(this, "Book not found");
			return;
		}
		txtIsbn.setText(current.getIsbn());
		txtTitle.setText(current.getTitle());
		txtAuthor.setText(current.getAuthor());
		txtGenre.setText(current.getGenre());
		spQuantity.setValue(current.getQuantity());
	}

	private void saveBook() {
		if (current == null) {
			JOptionPane.showMessageDialog(this, "Search a book first");
			return;
		}
		current.setTitle(txtTitle.getText().trim());
		current.setAuthor(txtAuthor.getText().trim());
		current.setGenre(txtGenre.getText().trim());
		current.setQuantity((Integer) spQuantity.getValue());
		JOptionPane.showMessageDialog(this, "Book updated");
		dispose();
	}
}
