package LibraryGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.Window; 

public class BookInfoCardFrame extends JDialog {

    private static final long serialVersionUID = 1L;

	public BookInfoCardFrame(Window parent, Book book) {
        super(parent, "Book Info Card", ModalityType.APPLICATION_MODAL);

		setSize(360, 280);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(204, 102, 0));
		JLabel lbl = new JLabel("  Book Info Card");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
		center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel cover = new JPanel();
		cover.setPreferredSize(new Dimension(120, 160));
		cover.setBackground(Color.CYAN);
		center.add(cover);

		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.add(new JLabel("ID: " + book.getId()));
		info.add(new JLabel(book.getTitle()));
		info.add(new JLabel(book.getAuthor()));
		info.add(new JLabel(book.getGenre()));
		info.add(new JLabel("Qty: " + book.getQuantity()));
		info.add(new JLabel("Available: " + (book.isAvailable() ? "YES" : "NO")));
		center.add(Box.createHorizontalStrut(10));
		center.add(info);

		add(center, BorderLayout.CENTER);
	}
}
