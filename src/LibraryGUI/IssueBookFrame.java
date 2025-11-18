package LibraryGUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;


public class IssueBookFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JSpinner spBookId   = new JSpinner(new SpinnerNumberModel(1, 0, 999999, 1));
	private JSpinner spMemberId = new JSpinner(new SpinnerNumberModel(1, 0, 999999, 1));

	private JLabel lblBookName   = new JLabel("Book Name");
	private JLabel lblMemberName = new JLabel("Member Full-Name");
	private JLabel lblAvailable  = new JLabel("Is This Book Available: ?");

	private LibrarySystemDashboard dashboard;

	public IssueBookFrame(LibrarySystemDashboard dashboard) {
		super(dashboard, "Issue Book", true);
		this.dashboard = dashboard;

		setSize(450, 280);
		setLocationRelativeTo(dashboard);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(120, 130, 150));
		JLabel lbl = new JLabel("  Issue Book");
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
		gbc.gridy = row; form.add(new JLabel("Enter Book ID:"), gbc);
		gbc.gridy++; 
		form.add(new JLabel(""), gbc);
		gbc.gridy++; 
		form.add(new JLabel("Enter Member ID:"), gbc);
		gbc.gridy++; 
		form.add(new JLabel(""), gbc);
		gbc.gridy++; 
		form.add(new JLabel(""), gbc);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 1; 
		gbc.gridy = 0;
		form.add(spBookId, gbc);
		gbc.gridy++; 
		form.add(lblBookName, gbc);
		gbc.gridy++; 
		form.add(spMemberId, gbc);
		gbc.gridy++; 
		form.add(lblMemberName, gbc);
		gbc.gridy++; 
		form.add(lblAvailable, gbc);

		JPanel btnPanel = new JPanel();
		JButton btnSearchBook   = new JButton("search book");
		JButton btnSearchMember = new JButton("search member");
		JButton btnIssue        = new JButton("Issue");
		JButton btnCancel       = new JButton("Cancel");
		btnPanel.add(btnSearchBook);
		btnPanel.add(btnSearchMember);
		btnPanel.add(btnIssue);
		btnPanel.add(btnCancel);

		gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		form.add(btnPanel, gbc);

		add(form, BorderLayout.CENTER);

		btnSearchBook.addActionListener(e -> loadBook());
		btnSearchMember.addActionListener(e -> loadMember());
		btnIssue.addActionListener(e -> issueBook());
		btnCancel.addActionListener(e -> dispose());
	}

	private void loadBook() {
		int id = (Integer) spBookId.getValue();
		Book found = null;
		for (Book b : LibraryData.BOOKS) 
			if (b.getId() == id) {
				found = b; 
				break; 
			}
		if (found == null) {
			lblBookName.setText("Book not found");
			lblAvailable.setText("Is This Book Available: ?");
		} else {
        	lblBookName.setText(found.getTitle());
        	lblAvailable.setText("Is This Book Available: " + (found.isAvailable() ? "YES" : "NO"));
        	lblAvailable.setForeground(found.isAvailable() ? Color.GREEN.darker() : Color.RED);
		}
	}

	private void loadMember() {
		int id = (Integer) spMemberId.getValue();
		Member found = null;
		for (Member m : LibraryData.MEMBERS) 
			if (m.getId() == id) {
				found = m; 
				break;
			}
		if (found == null) {
			lblMemberName.setText("Member not found");
		} else {
			lblMemberName.setText(found.getFirstName() + " " + found.getLastName());
		}
	}

    private void issueBook() {
        int bookId   = (Integer) spBookId.getValue();
        int memberId = (Integer) spMemberId.getValue();

        // 1) Find the book
        Book book = null;
        for (Book b : LibraryData.BOOKS) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }

        if (!book.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Book is NOT available");
            return;
        }

        // 2) Find the member
        Member member = null;
        for (Member m : LibraryData.MEMBERS) {
            if (m.getId() == memberId) {
                member = m;
                break;
            }
        }

        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found");
            return;
        }

        // 3) Mark as issued: set available=false, who has it, and due date
        int LOAN_DAYS = 14; // you can change this
        book.setAvailable(false);
        book.setIssuedToMemberId(member.getId());
        book.setDueDate(LocalDate.now().plusDays(LOAN_DAYS));

        JOptionPane.showMessageDialog(this,
                "Book issued to " + member.getFirstName() + " " + member.getLastName() +
                "\nDue date: " + book.getDueDate());

        dashboard.refreshDashboardCounts();
        dispose();
    }
}

