package gui;

import javax.swing.*;
import java.awt.*;

public class MemberAccountDialog extends JDialog {
    private static final long serialVersionUID = 1L;

	public MemberAccountDialog(Frame owner, Member member){
        super(owner, "My Holds & Fees", true);
        setSize(900, 560);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Holds", new HoldsPanel(member.getId()));
        tabs.addTab("My Late Fees", new LateFeesPanel(member.getId()));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
