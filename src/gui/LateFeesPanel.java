package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import client.ResponseHandler;
import message.*;
import message.Type;
import message.Action;
import message.Status;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LateFeesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
//	private final Integer memberIdFilter; // null -> show all
	private final ResponseHandler responseHandler;
	private final ObjectOutputStream requestWriter;
	private ArrayList<String> info;
	
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"#","Loan ID","Mdia ID","Title","Fee Ammount","Status","Paid Date"}, 0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel totalFees = new JLabel("Total Fees: $0.00");
//    private final JCheckBox cbPendingOnly = new JCheckBox("Pending only", true);

    public LateFeesPanel(ObjectOutputStream requestWriter, ResponseHandler responseHandler){
        super(new BorderLayout());
    	this.info = new ArrayList<>();
		this.responseHandler = responseHandler;
    	this.requestWriter = requestWriter;
    	
//        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        // requestRefresh();
    }

//    private JComponent buildTopBar(){
//        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JLabel title = new JLabel(memberIdFilter==null? "All Late Fees" : "My Late Fees");
//        p.add(title);
//        p.add(cbPendingOnly);
//        cbPendingOnly.addActionListener(e -> reload());
//        return p;
//    }
    
    private Window getParentWindow() {
    	return SwingUtilities.getWindowAncestor(this);
    }

//    private JComponent buildButtons(){
//        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton refresh = new JButton("Refresh");
//        JButton markPaid = new JButton("Mark Paid");
//        refresh.addActionListener(e -> requestRefresh());
//        markPaid.addActionListener(e -> markPaidSelected());
//        return p;
//    }
    
    private JPanel buildBottomBar() {
    	JPanel p = new JPanel(new BorderLayout());
    	
    	JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	JButton refresh = new JButton("Refresh");
    	JButton markPaid = new JButton("Mark Paid");
    	
    	refresh.addActionListener(e -> requestRefresh());
    	markPaid.addActionListener(e -> markPaidSelected());
    	
    	buttons.add(refresh);
    	buttons.add(markPaid);
    	
    	p.add(buttons, BorderLayout.EAST);
    	p.add(totalFees, BorderLayout.WEST);
    	
    	return p;
    }
    
    private void requestRefresh() {
    	if(requestWriter == null) {
    		return;
    	}
    	
    	Message msg = new Message(0, Type.REQUEST, -1, Action.GET_FEES, Status.PENDING, new ArrayList<>());
    	
    	responseHandler.setActiveLateFeesPanel(this);
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldWindow(getParentWindow());
    	
    	try {
    		requestWriter.writeObject(msg);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	
    }

    public void reload(ArrayList<String> info){
    	this.info = info;
        model.setRowCount(0);
        
        if (info == null || info.isEmpty()) {
        	totalFees.setText("Total Fees: $0.00");
        	return;
        }
        
        int count = Integer.parseInt(info.get(0));
        int index = 1;
        int rowNum = 1;
        double total = 0.0;
        
        for (int i = 0; i < count; i++){
        	int loanId = Integer.parseInt(info.get(index++));
        	int mediaId = Integer.parseInt(info.get(index++));
        	String title = info.get(index++);
        	double amount = Double.parseDouble(info.get(index++));
        	String status = info.get(index++);
        	String paidDate = info.get(index++);
        	
        	if (status.equalsIgnoreCase("UNPAID")) {
        		total += amount;
        	}
            
            model.addRow(new Object[]{
                    rowNum++,
                    loanId,
                    mediaId,
                    title, 
                    "$" + amount,
                    status,
                    paidDate.equals("-" != null ? "" : paidDate),
            });
        }
        if (model.getRowCount()==0){
            model.addRow(new Object[]{"", "(none)", "", "", "", "", "", ""});
        }
        totalFees.setText(String.format("Total Fees: $%.2f", total));
    }

    private void markPaidSelected(){
        int row = table.getSelectedRow();
        
        if (row < 0) {
        	JOptionPane.showMessageDialog(this, "Select a fee to mark as paid.");
        	return;
        }
        
        String status = model.getValueAt(row, 5).toString();
        if (!"UNPAID".equalsIgnoreCase(status)) {
        	JOptionPane.showMessageDialog(this, "Only UNPAID fees can be marked as paid.");
        	return;
        }
        
        int loadId = Integer.parseInt(model.getValueAt(row, 1).toString());
        
        ArrayList<String> list = new ArrayList<>();
        list.add(Integer.toString(loadId));
        
        Message msg = new Message(0, Type.REQUEST, -1, Action.PAY_FEES, Status.PENDING, list);
        
        responseHandler.setActiveLateFeesPanel(this);
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldWindow(getParentWindow());
        
        try {
        	requestWriter.writeObject(msg);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
}
