package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class LibrarySystemDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private final User currentUser;
	private JLabel lblBooksCount   = new JLabel("0", SwingConstants.CENTER);
	private JLabel lblMembersCount = new JLabel("0", SwingConstants.CENTER);
	private JLabel lblAuthorsCount = new JLabel("0", SwingConstants.CENTER);

	public LibrarySystemDashboard(User user) {
		this.currentUser = user;

		setTitle("Library System Dashboard");
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// TOP
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(new Color(38, 70, 120));
		JLabel lblTitle = new JLabel("  Library System Dashboard");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
		JLabel lblWelcome = new JLabel("Welcome Back! [ " + currentUser.getUsername() + " ]  ");
		lblWelcome.setForeground(Color.WHITE);
		top.add(lblTitle, BorderLayout.WEST);
		top.add(lblWelcome, BorderLayout.EAST);
		add(top, BorderLayout.NORTH);

		// LEFT MENU
		JPanel left = new JPanel();
		left.setBackground(new Color(25, 30, 40));
		left.setPreferredSize(new Dimension(200, 600));
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

		JLabel lblLogo = new JLabel("   \uD83D\uDCD8 Library");
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setFont(lblLogo.getFont().deriveFont(Font.BOLD, 18f));
		lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
		lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
		left.add(lblLogo);

		left.add(menuSectionTitle("Genres"));
		left.add(menuButton("Manage Genres", () ->
             	new ManageGenresFrame(this).setVisible(true)));

		left.add(menuSectionTitle("Authors"));
		left.add(menuButton("Manage Authors", () ->
				new ManageAuthorsFrame(this).setVisible(true)));

		left.add(menuSectionTitle("Members"));
		left.add(menuButton("Add Member", () -> new AddMemberFrame(this).setVisible(true)));
		left.add(menuButton("Edit Member", () -> new EditMemberFrame(this).setVisible(true)));
		left.add(menuButton("Delete Member", () -> new ManageMembersFrame(this).setVisible(true)));
		left.add(menuButton("Members List", () -> new MembersListFrame(this).setVisible(true)));
		left.add(menuButton("Manage Members", () -> new ManageMembersFrame(this).setVisible(true)));

		left.add(menuSectionTitle("Books"));
		left.add(menuButton("Add Book", () -> new AddBookFrame(this).setVisible(true)));
		left.add(menuButton("Edit Book", () -> new EditBookFrame(this).setVisible(true)));
		left.add(menuButton("Delete Book", () -> new ManageBooksFrame(this).setVisible(true)));
		left.add(menuButton("Books List", () -> new BooksListFrame(this).setVisible(true)));
		left.add(menuButton("Manage Books", () -> new ManageBooksFrame(this).setVisible(true)));

		left.add(menuSectionTitle("Circulation"));
		left.add(menuButton("Issue Book", () -> new IssueBookFrame(this).setVisible(true)));
		left.add(menuButton("Return Book", () -> new ReturnBookFrame(this).setVisible(true)));

		if (currentUser.getRole() == User.Role.ADMIN) {
			left.add(menuSectionTitle("Manage Users"));
			left.add(menuButton("Manage Users", () -> new ManageUsersFrame(this).setVisible(true)));
		}

		JButton btnLogout = menuButton("Logout", () -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
		left.add(Box.createVerticalStrut(10));
		left.add(btnLogout);

		add(left, BorderLayout.WEST);

		// CENTER
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		center.setBackground(new Color(235, 239, 246));

		JPanel statsRow = new JPanel(new GridLayout(1, 3, 15, 0));
		statsRow.setOpaque(false);
		statsRow.add(coloredCard("Books",   lblBooksCount,   new Color(255, 153, 51)));
		statsRow.add(coloredCard("Members", lblMembersCount, new Color(102, 204, 0)));
		statsRow.add(coloredCard("Authors", lblAuthorsCount, new Color(153, 102, 255)));
		center.add(statsRow);
		center.add(Box.createVerticalStrut(20));

		JPanel latestPanel = new JPanel(new BorderLayout());
		latestPanel.setOpaque(false);
		JLabel latestTitle = new JLabel("Latest Books Added");
		latestTitle.setFont(latestTitle.getFont().deriveFont(Font.BOLD, 16f));
		latestPanel.add(latestTitle, BorderLayout.NORTH);

		JPanel bookCoversRow = new JPanel();
		bookCoversRow.setBackground(new Color(52, 119, 196));
		bookCoversRow.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));

		for (Book b : LibraryData.BOOKS) {
			JPanel cover = new JPanel(new BorderLayout());
			cover.setPreferredSize(new Dimension(110, 150));
			cover.setBackground(Color.WHITE);
			cover.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
			JLabel lbl = new JLabel("<html><center>" + b.getTitle() + "</center></html>");
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			cover.add(lbl, BorderLayout.CENTER);
			bookCoversRow.add(cover);
		}

		JScrollPane scrollCovers = new JScrollPane(bookCoversRow);
		scrollCovers.setPreferredSize(new Dimension(600, 200));
		scrollCovers.setBorder(null);
		latestPanel.add(scrollCovers, BorderLayout.CENTER);

		center.add(latestPanel);

		add(center, BorderLayout.CENTER);

		// STATUS
		JPanel status = new JPanel(new BorderLayout());
		status.setBackground(new Color(90, 90, 90));
		JLabel lblUser = new JLabel("  Logged in as: " + currentUser.getUsername());
		lblUser.setForeground(Color.WHITE);
		status.add(lblUser, BorderLayout.WEST);
		add(status, BorderLayout.SOUTH);

		updateCounts();
	}

	private JLabel menuSectionTitle(String text) {
		JLabel label = new JLabel("  " + text);
		label.setForeground(Color.LIGHT_GRAY);
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private JButton menuButton(String text, Runnable action) {
		JButton btn = new JButton(text);
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn.setMaximumSize(new Dimension(180, 28));
		btn.setBackground(new Color(43, 49, 60));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 3));
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.addActionListener(e -> action.run());
    	return btn;
	}

	private JPanel coloredCard(String title, JLabel valueLabel, Color color) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(150, 100));
		panel.setBackground(color);

		JLabel lblTitle = new JLabel(" " + title);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

		valueLabel.setForeground(Color.WHITE);
		valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 28f));

		panel.add(lblTitle, BorderLayout.NORTH);
		panel.add(valueLabel, BorderLayout.CENTER);
		return panel;
	}

	private void updateCounts() {
		lblBooksCount.setText(String.valueOf(LibraryData.BOOKS.size()));
		lblMembersCount.setText(String.valueOf(LibraryData.MEMBERS.size()));
		lblAuthorsCount.setText(String.valueOf(LibraryData.AUTHORS.size()));
	}

	public void refreshDashboardCounts() {
		updateCounts();
	}
}
