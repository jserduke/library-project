package LibraryGUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class ReturnBookFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JSpinner spBookId = new JSpinner(new SpinnerNumberModel(1, 0, 999999, 1));
	private JLabel lblBookName = new JLabel("Book Name / Status");

	private LibrarySystemDashboard dashboard;

	public ReturnBookFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Return Book", true);
		this.dashboard = dashboard;

		setSize(420, 220);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(140, 150, 170));
		JLabel lbl = new JLabel("  Return Book");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.LINE_END;

		gbc.gridx = 0; gbc.gridy = 0;
		form.add(new JLabel("Book ID:"), gbc);
		gbc.gridy++;
		form.add(new JLabel(""), gbc);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 1; 
		gbc.gridy = 0;
		form.add(spBookId, gbc);
		gbc.gridy++;
		form.add(lblBookName, gbc);

		JPanel btnPanel = new JPanel();
		JButton btnLoad   = new JButton("Load");
		JButton btnReturn = new JButton("Return");
		JButton btnLost   = new JButton("Lost");
		JButton btnCancel = new JButton("Cancel");
		btnPanel.add(btnLoad);
		btnPanel.add(btnReturn);
		btnPanel.add(btnLost);
		btnPanel.add(btnCancel);

		gbc.gridx = 0; 
		gbc.gridy++; 
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		form.add(btnPanel, gbc);

		add(form, BorderLayout.CENTER);

		btnLoad.addActionListener(e -> loadBook());
		btnReturn.addActionListener(e -> returnBook());
		btnLost.addActionListener(e -> markLost());
		btnCancel.addActionListener(e -> dispose());
	}

	private Book findBookById(int id) {
		for (Book b : LibraryData.BOOKS) 
			if (b.getId() == id) 
				return b;
		return null;
	}

	private void loadBook() {
		int id = (Integer) spBookId.getValue();
		Book b = findBookById(id);
		if (b == null) {
			lblBookName.setText("Book not found");
		} else {
			lblBookName.setText(b.getTitle() + " (" + (b.isAvailable() ? "available" : "issued") + ")");
		}
	}

    private void returnBook() {
        int bookId = (Integer) spBookId.getValue();
        Book book = findBookById(bookId);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }

        if (book.isAvailable()) {
            JOptionPane.showMessageDialog(this, "This book is already marked as AVAILABLE.");
            return;
        }

        // Defaults
        double fee = 0.0;
        long daysLate = 0;

        // 1) Figure out who had the book
        Member member = null;
        if (book.getIssuedToMemberId() != null) {
            int memberId = book.getIssuedToMemberId();
            for (Member m : LibraryData.MEMBERS) {
                if (m.getId() == memberId) {
                    member = m;
                    break;
                }
            }
        }

        // 2) Check if overdue
        LocalDate today   = LocalDate.now();
        LocalDate dueDate = book.getDueDate();

        if (dueDate != null && today.isAfter(dueDate)) {
            daysLate = ChronoUnit.DAYS.between(dueDate, today);
            double DAILY_FEE = 1.0; // $1 per day late (change if you want)
            fee = daysLate * DAILY_FEE;

            if (member != null) {
                member.addToBalance(fee);
            }
        }

        // 3) Mark book as returned
        book.setAvailable(true);
        book.setIssuedToMemberId(null);
        book.setDueDate(null);

        dashboard.refreshDashboardCounts();

        // 4) Show message to user
        if (fee > 0 && member != null) {
            JOptionPane.showMessageDialog(this,
                    "Book returned.\n" +
                    "Late by " + daysLate + " day(s).\n" +
                    "Late fee added: $" + String.format("%.2f", fee) + "\n" +
                    "Member balance now: $" + String.format("%.2f", member.getBalance()));
        } else if (fee > 0) {
            JOptionPane.showMessageDialog(this,
                    "Book returned late by " + daysLate +
                    " day(s). Late fee: $" + String.format("%.2f", fee));
        } else {
            JOptionPane.showMessageDialog(this, "Book returned on time. No late fee.");
        }

        dispose();
    }


	private void markLost() {
		int id = (Integer) spBookId.getValue();
		Book b = findBookById(id);
		if (b == null) {
			JOptionPane.showMessageDialog(this, "Book not found");
			return;
		}
		b.setAvailable(false);
		JOptionPane.showMessageDialog(this, "Book marked as LOST.");
		dispose();
	}
}
