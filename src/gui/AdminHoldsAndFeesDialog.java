package gui;

import javax.swing.*;
import java.awt.*;

public class AdminHoldsAndFeesDialog extends JDialog {
    private static final long serialVersionUID = 1L;

	public AdminHoldsAndFeesDialog(Frame owner){
        super(owner, "Holds & Fees", true);
        setSize(900, 560);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overdues", new OverduesPanel());
        tabs.addTab("Holds", new HoldsPanel(null));
        tabs.addTab("Late Fees", new LateFeesPanel(null));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
