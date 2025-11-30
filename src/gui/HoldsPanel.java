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
	private JFrame getParentFrame;
//    private final int memberId; // null -> show all
	
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
    	
//    	setLayout(new BorderLayout());
//    	add(new JScrollPane(table), BorderLayout.CENTER);
//    	JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//    	JButton refresh = new JButton("Refresh");
//    	JButton cancel = new JButton("Cancel hold");
//    	
//    	bottom.add(refresh);
//    	add(bottom, BorderLayout.SOUTH);
//    	refresh.addActionListener(e -> reload(this.info));
//    	cancel.addActionListener(e -> reload(this.info));
    	reload(info);
    }
    
    //Constructor for ADMIN
    public HoldsPanel(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
    	super(new BorderLayout());
    	this.info = info;
		this.responseHandler = responseHandler;
    	this.requestWriter = requestWriter;
    	
    	responseHandler.setActiveHoldsPanel(this);
    	
    	add(buildTopBar(), BorderLayout.NORTH);
    	add(new JScrollPane(table), BorderLayout.CENTER);
    	add(buildButtons(), BorderLayout.SOUTH);
    	
    	Message msg = new Message(0, Type.REQUEST, -1, Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
  
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
//        super(new BorderLayout());
//        this.memberId = memberId;
//		this.responseHandler = null;
//		this.requestWriter = null;
//		
//        add(buildTopBar(), BorderLayout.NORTH);
//        add(new JScrollPane(table), BorderLayout.CENTER);
//        add(buildButtons(), BorderLayout.SOUTH);
//        reload(info);
    }

    private JComponent buildTopBar(){
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JLabel title = new JLabel(memberIdFilter==null? "All Holds" : "My Holds");
        p.add(new Label("My Holds"));
        p.add(cbActiveOnly);
        cbActiveOnly.addActionListener(e -> reload(info));
        return p;
    }

    private JComponent buildButtons(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refresh = new JButton("Refresh");
        JButton cancel = new JButton("Cancel Hold");
        
//        p.add(refresh); p.add(cancel);
        refresh.addActionListener(e -> requestRefresh());
        cancel.addActionListener(e -> requestCancel());
        
        p.add(refresh); 
        p.add(cancel);
        return p;
    }
    
    private JComponent buildAdminButtons() {
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	JButton refresh = new JButton("Refresh");
    	
    	JButton cancel = new JButton("Cancel Hold");
        
//      p.add(refresh); p.add(cancel);
    	refresh.addActionListener(e -> reload(info));
    	cancel.addActionListener(e -> requestCancel());
      
    	p.add(refresh); 
    	p.add(cancel);
    	return p;
    }

//    private String findMemberName(int id){
//        for (Member m : LibraryData.MEMBERS){
//            if (m.getId()==id){
//                return m.getFirstName() + " " + m.getLastName() + " (#"+id+")";
//            }
//        }
//        return "Member #"+id;
//    }

//    private String itemTitle(MediaType t, int id){
//        switch (t){
//            case BOOK:
//                for (Book b : LibraryData.BOOKS) 
//                	if (b.getId()==id)
//                		return b.getTitle();
//                break;
//            case DVD:
//                for (Dvd d : LibraryData.DVDS) 
//                	if (d.getId()==id) 
//                		return d.getTitle();
//                break;
//            case BOARD_GAME:
//                for (BoardGame g : LibraryData.BOARD_GAMES) 
//                	if (g.getId()==id) 
//                		return g.getTitle();
//                break;
//        }
//        return t+" #"+id;
//    }

//    public void reload(){
//        model.setRowCount(0);
//        LocalDate today = LocalDate.now();
//        int rowNum = 1;
//        for (Hold h : LibraryData.HOLDS){
//            if (memberIdFilter != null && h.getMemberId() != memberIdFilter.intValue()) 
//            	continue;
//            LocalDate until = h.getHoldUntil();
//            boolean active = until != null && !today.isAfter(until);
//            if (cbActiveOnly.isSelected() && !active) 
//            	continue;
//            long daysLeft = until == null ? 0 : ChronoUnit.DAYS.between(today, until);
//            model.addRow(new Object[]{
//                    rowNum++,
//                    findMemberName(h.getMemberId()),
//                    itemTitle(h.getMediaType(), h.getMediaId()),
//                    h.getMediaType().name(),
//                    until,
//                    active? "ACTIVE" : "EXPIRED",
//                    active? daysLeft : 0
//            });
//        }
//        if (model.getRowCount()==0){
//            model.addRow(new Object[]{"", "(none)", "", "", "", "", ""});
//        }
//    }
    
    private JFrame getParentFrame() {
    	Window w = SwingUtilities.getWindowAncestor(this);
    	return (w instanceof JFrame) ? (JFrame) w: null;
    }
    
    private Window parentWindow() {
    	return SwingUtilities.getWindowAncestor(this);
    }
    
    private void requestRefresh() {
    	if (requestWriter == null) {
    		return;
    	}
    	
    	Message msg = new Message(0, Type.REQUEST, -1, Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
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
    	Message msg = new Message(0, Type.REQUEST, -1, Action.CANCEL_HOLD, Status.PENDING, info);
    	
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

//    private void cancelSelected(){
//        int row = table.getSelectedRow();
//        if (row < 0)
//        	return;
//        Object memberName = model.getValueAt(row, 1);
//        Object itemType = model.getValueAt(row, 3);
//        Object itemTitle = model.getValueAt(row, 2);
//        Object untilObj = model.getValueAt(row, 4);
//        if (!(itemType instanceof String)) 
//        	return;
//        MediaType type = MediaType.valueOf((String)itemType);
//        LocalDate until = (untilObj instanceof LocalDate) ? (LocalDate)untilObj : null;
//
//        // Find the first hold that matches member+item+until
//        for (Hold h : LibraryData.HOLDS){
//            String candidateTitle = itemTitle(type, h.getMediaId());
//            if ((memberName+"").contains("#"+h.getMemberId()+")") &&
//                candidateTitle.equals(itemTitle) &&
//                h.getMediaType()==type &&
//                ((h.getHoldUntil()==null && until==null) || (h.getHoldUntil()!=null && h.getHoldUntil().equals(until)))
//            ){
//                int confirm = JOptionPane.showConfirmDialog(this, "Cancel hold for "+candidateTitle+"?", "Confirm", JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION){
//                    LibraryData.HOLDS.remove(h);
//                    reload(info);
//                }
//                return;
//            }
//        }
//    }
}
