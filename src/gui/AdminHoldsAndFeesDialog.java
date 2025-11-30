package gui;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import client.ResponseHandler;
import message.*;
import message.Type;
import account.HoldStatus;

public class AdminHoldsAndFeesDialog extends JDialog {
    private static final long serialVersionUID = 1L;

	public AdminHoldsAndFeesDialog(Frame owner, ObjectOutputStream requestWriter, ResponseHandler responseHandler){
        super(owner, "Holds & Fees", true);
        setSize(900, 560);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();
        HoldsPanel holdsPanel = new HoldsPanel(requestWriter, responseHandler);
        responseHandler.setActiveHoldsPanel(holdsPanel);
        
//        tabs.addTab("Overdues", new OverduesPanel());
        tabs.addTab("All Holds", holdsPanel);
        tabs.addTab("Late Fees", new LateFeesPanel(requestWriter, responseHandler));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        
        sendInitialGetHolds(requestWriter, responseHandler);
    }
	
	private void sendInitialGetHolds(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
		try {
			Message msg = new Message(message.Type.REQUEST, -1, message.Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
	    	responseHandler.setRequestIdExpected(msg.getId());
	    	requestWriter.writeObject(msg);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
