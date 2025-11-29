package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import client.*;
import message.*;
import message.Status;

public class MemberAccountDialog extends JDialog {
    private static final long serialVersionUID = 1L;
//    private final ResponseHandler responseHandler;
//	private final ObjectOutputStream requestWriter;
//	private HoldsPanel holdsPanel;

//	public MemberAccountDialog(Frame owner, int memberId){
//        super(owner, "My Holds & Fees", true);
//        setSize(900, 560);
//        setLocationRelativeTo(owner);
//
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.addTab("My Holds", new HoldsPanel(memberId));
//        tabs.addTab("My Late Fees", new LateFeesPanel(memberId));
//
//        setLayout(new BorderLayout());
//        add(tabs, BorderLayout.CENTER);
//    }
    
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

//      setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
        	@Override
        	public void windowClosed(java.awt.event.WindowEvent e) {
        		responseHandler.setOldDialog(null);
        	}
        });
//        add(tabs);
        
//        responseHandler.setActiveHoldsPanel(holdsPanel);
        
//        ArrayList<String> info = new ArrayList<>();
//        Message msg = new Message(0, message.Type.REQUEST, -1, message.Action.GET_HOLDS, Status.PENDING, info);
//        
//        responseHandler.setRequestIdExpected(msg.getId());
//        responseHandler.setOldFrame(this);
//        
//        try {
//        	requestWriter.writeObject(msg);
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//    responseHandler.setRequestIdExpected(-1);
//    responseHandler.setOldWindow(this);
    }
}
