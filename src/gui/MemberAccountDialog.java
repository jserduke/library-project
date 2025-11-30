package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.ObjectOutputStream;

import client.*;

public class MemberAccountDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public MemberAccountDialog(Frame owner,
    						   ObjectOutputStream requestWriter,
    						   ResponseHandler responseHandler,
    						   ArrayList<String> info) {
        super(owner, "My Holds & Fees", true);
        setSize(900, 560);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        JTabbedPane tabs = new JTabbedPane();
        HoldsPanel holdsPanel = new HoldsPanel(info, requestWriter, responseHandler);
        
       responseHandler.setActiveHoldsPanel(holdsPanel);
       responseHandler.setOldDialog(this);
        
        tabs.addTab("My Holds", holdsPanel);
        tabs.addTab("My Late Fees", new LateFeesPanel(requestWriter, responseHandler));
        add(tabs, BorderLayout.CENTER);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
        	@Override
        	public void windowClosing(java.awt.event.WindowEvent e) {
        		responseHandler.setOldDialog(null);
        		responseHandler.setActiveHoldsPanel(null);
        	}
        });
    }
}
