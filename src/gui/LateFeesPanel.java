package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LateFeesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private final Integer memberIdFilter; // null -> show all
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Fee ID","Member","Loan ID","Item","Days Late","Amount","Status","Assessed"}, 0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);
    private final JCheckBox cbPendingOnly = new JCheckBox("Pending only", true);

    public LateFeesPanel(Integer memberIdFilter){
        super(new BorderLayout());
        this.memberIdFilter = memberIdFilter;
        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        reload();
    }

    private JComponent buildTopBar(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel(memberIdFilter==null? "All Late Fees" : "My Late Fees");
        p.add(title);
        p.add(cbPendingOnly);
        cbPendingOnly.addActionListener(e -> reload());
        return p;
    }

    private JComponent buildButtons(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refresh = new JButton("Refresh");
        JButton markPaid = new JButton("Mark Paid");
        JButton waive = new JButton("Waive...");
        p.add(refresh); p.add(markPaid); p.add(waive);
        refresh.addActionListener(e -> reload());
        markPaid.addActionListener(e -> markPaidSelected());
        waive.addActionListener(e -> waiveSelected());
        return p;
    }

    private String findMemberName(int id){
        for (Member m : LibraryData.MEMBERS){
            if (m.getId()==id){
                return m.getFirstName() + " " + m.getLastName() + " (#"+id+")";
            }
        }
        return "Member #"+id;
    }

    private String itemTitle(MediaType t, int id){
        switch (t){
            case BOOK:
                for (Book b : LibraryData.BOOKS) 
                	if (b.getId()==id) 
                		return b.getTitle();
                break;
            case DVD:
                for (Dvd d : LibraryData.DVDS) 
                	if (d.getId()==id) 
                		return d.getTitle();
                break;
            case BOARD_GAME:
                for (BoardGame g : LibraryData.BOARD_GAMES) 
                	if (g.getId()==id) 
                		return g.getTitle();
                break;
        }
        return t+" #"+id;
    }

    public void reload(){
        model.setRowCount(0);
        for (LateFee f : FeeStore.LATE_FEES){
            if (memberIdFilter != null && f.getMemberId() != memberIdFilter.intValue()) 
            	continue;
            if (cbPendingOnly.isSelected() && f.getStatus() != LateFee.Status.PENDING) 
            	continue;
            model.addRow(new Object[]{
                    f.getId(),
                    findMemberName(f.getMemberId()),
                    f.getLoanId(),
                    itemTitle(f.getMediaType(), f.getMediaId()),
                    f.getDaysLate(),
                    f.getAmountFormatted(),
                    f.getStatus().name(),
                    f.getAssessedAt()
            });
        }
        if (model.getRowCount()==0){
            model.addRow(new Object[]{"", "(none)", "", "", "", "", "", ""});
        }
    }

    private void markPaidSelected(){
        int row = table.getSelectedRow();
        if (row < 0)
        	return;
        
        Object val = model.getValueAt(row, 0);
        if (!(val instanceof Integer))
        	return;
        
        int id = (Integer) val;
        FeeStore.markPaid(id);
        reload();
    }

    private void waiveSelected(){
    	
        int row = table.getSelectedRow();
        if (row < 0) 
        	return;
        
        Object val = model.getValueAt(row, 0);
        if (!(val instanceof Integer)) 
        	return;
        
        int id = (Integer) val;
        String reason = JOptionPane.showInputDialog(this, "Waive reason:");
        if (reason == null) 
        	return;
        FeeStore.waive(id, reason);
        reload();
    }
}
