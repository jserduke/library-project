package gui;

import client.ResponseHandler;
import message.*;
import message.Type;
import message.Action;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import account.HoldStatus;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class HoldsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final ResponseHandler responseHandler;
	private final ObjectOutputStream requestWriter;
	private ArrayList<String> info;
	private Integer memberIdFilter; // null -> show all
	
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"#", "Hold ID","Media ID","Title","Hold Until","Status"}, 0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    
    private final JTable table = new JTable(model);
    private final JCheckBox cbActiveOnly = new JCheckBox("Active only", true);

    public HoldsPanel(ArrayList<String> info,
    				  ObjectOutputStream requestWriter, 
    				  ResponseHandler responseHandler) {
    	super(new BorderLayout());
    	this.info = info;
		this.responseHandler = responseHandler;
    	this.requestWriter = requestWriter;
    	
    	responseHandler.setActiveHoldsPanel(this);
    	
    	add(buildTopBar(), BorderLayout.NORTH);
    	add(new JScrollPane(table), BorderLayout.CENTER);
    	add(buildButtons(), BorderLayout.SOUTH);
    	
    	reload(info);
    }
    
    //Constructor for ADMIN
    public HoldsPanel(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
    	super(new BorderLayout());
//    	this.info = info;
		this.responseHandler = responseHandler;
    	this.requestWriter = requestWriter;
    	
    	responseHandler.setActiveHoldsPanel(this);
    	
    	add(buildTopBar(), BorderLayout.NORTH);
    	add(new JScrollPane(table), BorderLayout.CENTER);
    	add(buildButtons(), BorderLayout.SOUTH);
    	
    	Message msg = new Message(Type.REQUEST, -1, Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
  
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldWindow(SwingUtilities.getWindowAncestor(this));
    	
    	try {
    		requestWriter.writeObject(msg);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public HoldsPanel(Integer adminMode) {
    	super(new BorderLayout());
    	this.info = new ArrayList<>();
    	this.requestWriter = null;
    	this.responseHandler = null;
    	
    	add(new JScrollPane(table), BorderLayout.CENTER);
    	reload(info);
    }
    
    public HoldsPanel(int memberIdFilter){
    	this(new ArrayList<>(), null, null);
    	this.memberIdFilter = memberIdFilter;
    }

    private JComponent buildTopBar(){
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p.add(new Label("My Holds"));
        p.add(cbActiveOnly);
        cbActiveOnly.addActionListener(e -> reload(info));
        return p;
    }

    private JComponent buildButtons(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // JButton refresh = new JButton("Refresh");
        JButton cancel = new JButton("Cancel Hold");
        
//        p.add(refresh); p.add(cancel);
        // refresh.addActionListener(e -> requestRefresh());
        cancel.addActionListener(e -> requestCancel());
        
        // p.add(refresh); 
        p.add(cancel);
        return p;
    }
    
    private JComponent buildAdminButtons() {
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	// JButton refresh = new JButton("Refresh");
    	
    	JButton cancel = new JButton("Cancel Hold");
        
//      p.add(refresh); p.add(cancel);
    	// refresh.addActionListener(e -> reload(info));
    	cancel.addActionListener(e -> requestCancel());
      
    	// p.add(refresh); 
    	p.add(cancel);
    	return p;
    }
    
    private Window parentWindow() {
    	return SwingUtilities.getWindowAncestor(this);
    }
    
    private void requestRefresh() {
    	if (requestWriter == null) {
    		return;
    	}
    	
    	Message msg = new Message(Type.REQUEST, -1, Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
    	responseHandler.setActiveHoldsPanel(this);
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldWindow(parentWindow());
    	
    	try {
    		requestWriter.writeObject(msg);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void requestCancel() {
    	if (requestWriter == null) {
    		return;
    	}
    	
    	int row = table.getSelectedRow();
    	if (row < 0) {
    		JOptionPane.showMessageDialog(this, "Select a hold to cancel.");
    		return;
    	}
    	
    	String statusStr = model.getValueAt(row, 5).toString();
    	HoldStatus status = HoldStatus.valueOf(statusStr.toUpperCase());
    	if (status != HoldStatus.ACTIVE) {
    		JOptionPane.showMessageDialog(this, "Only ACTIVE holds can be cancelled.");
    		return;
    	}
    	
    	int holdId = (int) model.getValueAt(row, 1);
    	
    	ArrayList<String> info = new ArrayList<>();
    	info.add(Integer.toString(holdId));
    	Message msg = new Message(Type.REQUEST, -1, Action.CANCEL_HOLD, Status.PENDING, info);
    	
    	responseHandler.setActiveHoldsPanel(this);
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldWindow(parentWindow());
    	
    	try {
    		requestWriter.writeObject(msg);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void reload(ArrayList<String> info) {
    	this.info = info;
    	
    	model.setRowCount(0);
    	
    	if (info == null || info.isEmpty()) {
    		model.addRow(new Object[] {"", "(none)", "", "", "", ""});
    		return;
    	}
    	
    	int count = Integer.parseInt(info.get(0));
    	int index = 1;
    	int rowNum = 1;
    	
    	for (int i = 0; i < count; i++) {
    		int holdId = Integer.parseInt(info.get(index++));
    		int mediaId = Integer.parseInt(info.get(index++));
    		String title = info.get(index++);
    		long untilMillis = Long.parseLong(info.get(index++));
    		String statusStr = info.get(index++);    		    		
    		
    		HoldStatus status = HoldStatus.valueOf(statusStr.toUpperCase());
    		if (cbActiveOnly.isSelected() && status != HoldStatus.ACTIVE) {
    			continue;
    		}
    		
    		model.addRow(new Object[] {
    				rowNum++,
    				holdId,
    				mediaId,
    				title,
    				new Date(untilMillis),
    				status
    		});
    	}
    	
    	if (model.getRowCount() == 0) {
    		model.addRow(new Object[] {"", "(none)", "", "", "", "", ""});
    	}
    }
}
