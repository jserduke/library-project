package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class AddBookFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtIsbn   = new JTextField(10);
	private JTextField txtTitle  = new JTextField(20);
	private JTextField txtAuthor = new JTextField(15);
	private JTextField txtGenre  = new JTextField(10);
 	private JSpinner spQuantity  =
 			new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

 	private LibrarySystemDashboard dashboard;

 	public AddBookFrame(LibrarySystemDashboard dashboard) {
 		super(dashboard, "Add Book", true);
 		this.dashboard = dashboard;

 		setSize(550, 280);
 		setLocationRelativeTo(dashboard);
 		setLayout(new BorderLayout());

 		JPanel header = new JPanel(new BorderLayout());
 		header.setBackground(new Color(204, 102, 0));
 		JLabel lbl = new JLabel("  Add a New Book");
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
 		form.add(new JLabel("ISBN:"), gbc);
 		gbc.gridy++; form.add(new JLabel("Name:"), gbc);
 		gbc.gridy++; form.add(new JLabel("Author:"), gbc);
 		gbc.gridy++; form.add(new JLabel("Genre:"), gbc);
 		gbc.gridy++; form.add(new JLabel("Quantity:"), gbc);

 		gbc.anchor = GridBagConstraints.LINE_START;
 		gbc.gridx = 1; 
 		gbc.gridy = 0;
     	form.add(txtIsbn, gbc);
     	gbc.gridy++; form.add(txtTitle, gbc);
     	gbc.gridy++; form.add(txtAuthor, gbc);
     	gbc.gridy++; form.add(txtGenre, gbc);
     	gbc.gridy++; form.add(spQuantity, gbc);

     	JPanel btnPanel = new JPanel();
     	JButton btnClear  = new JButton("Clear");
     	JButton btnAdd    = new JButton("Add Book");
     	JButton btnCancel = new JButton("Cancel");
     	btnPanel.add(btnClear);
     	btnPanel.add(btnAdd);
     	btnPanel.add(btnCancel);

     	gbc.gridx = 0; 
     	gbc.gridy++;
     	gbc.gridwidth = 2;
     	gbc.anchor = GridBagConstraints.CENTER;
     	form.add(btnPanel, gbc);

     	add(form, BorderLayout.CENTER);

     	btnClear.addActionListener(e -> clearForm());
     	btnCancel.addActionListener(e -> dispose());
     	btnAdd.addActionListener(e -> addBook());
 	}

 	private void clearForm() {
 		txtIsbn.setText("");
 		txtTitle.setText("");
 		txtAuthor.setText("");
 		txtGenre.setText("");
 		spQuantity.setValue(1);
 	}
 	
 	private void addBook() {
 		int id = LibraryData.nextBookId();
 		Book b = new Book(
 				id,
 				txtIsbn.getText().trim(),
 				txtTitle.getText().trim(),
 				txtAuthor.getText().trim(),
 				txtGenre.getText().trim(),
 				(Integer) spQuantity.getValue(),
 				true
 				);
 		LibraryData.BOOKS.add(b);
 		JOptionPane.showMessageDialog(this, "Book added with ID: " + id);
 		dashboard.refreshDashboardCounts();
 		dispose();
 	}
}

