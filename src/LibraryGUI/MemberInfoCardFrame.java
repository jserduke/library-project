package LibraryGUI;

import javax.swing.*;
import java.awt.*;

public class MemberInfoCardFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	public MemberInfoCardFrame(JFrame parent, Member member) {
		super(parent, "Member Info Card", true);

		setSize(320, 260);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(0, 153, 102));
		JLabel lbl = new JLabel("  Member Info Card");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
		header.add(lbl, BorderLayout.WEST);
		add(header, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
		center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel imgPanel = new JPanel();
		imgPanel.setPreferredSize(new Dimension(100, 120));
		imgPanel.setBackground(Color.ORANGE);
		center.add(imgPanel);

		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.add(new JLabel("ID: " + member.getId()));
		info.add(new JLabel(member.getFirstName() + " " + member.getLastName()));
		info.add(new JLabel(member.getPhone()));
		info.add(new JLabel(member.getEmail()));
		info.add(new JLabel(member.getGender()));
		center.add(Box.createHorizontalStrut(10));
		center.add(info);

		add(center, BorderLayout.CENTER);
	}
}
