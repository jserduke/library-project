package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HoldsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private final Integer memberIdFilter; // null -> show all
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"#", "Member","Item","Type","Hold Until","Status","Days Left"}, 0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);
    private final JCheckBox cbActiveOnly = new JCheckBox("Active only", true);

    public HoldsPanel(Integer memberIdFilter){
        super(new BorderLayout());
        this.memberIdFilter = memberIdFilter;
        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        reload();
    }

    private JComponent buildTopBar(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel(memberIdFilter==null? "All Holds" : "My Holds");
        p.add(title);
        p.add(cbActiveOnly);
        cbActiveOnly.addActionListener(e -> reload());
        return p;
    }

    private JComponent buildButtons(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refresh = new JButton("Refresh");
        JButton cancel = new JButton("Cancel Hold");
        p.add(refresh); p.add(cancel);
        refresh.addActionListener(e -> reload());
        cancel.addActionListener(e -> cancelSelected());
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
        LocalDate today = LocalDate.now();
        int rowNum = 1;
        for (Hold h : LibraryData.HOLDS){
            if (memberIdFilter != null && h.getMemberId() != memberIdFilter.intValue()) 
            	continue;
            LocalDate until = h.getHoldUntil();
            boolean active = until != null && !today.isAfter(until);
            if (cbActiveOnly.isSelected() && !active) 
            	continue;
            long daysLeft = until == null ? 0 : ChronoUnit.DAYS.between(today, until);
            model.addRow(new Object[]{
                    rowNum++,
                    findMemberName(h.getMemberId()),
                    itemTitle(h.getMediaType(), h.getMediaId()),
                    h.getMediaType().name(),
                    until,
                    active? "ACTIVE" : "EXPIRED",
                    active? daysLeft : 0
            });
        }
        if (model.getRowCount()==0){
            model.addRow(new Object[]{"", "(none)", "", "", "", "", ""});
        }
    }

    private void cancelSelected(){
        int row = table.getSelectedRow();
        if (row < 0)
        	return;
        Object memberName = model.getValueAt(row, 1);
        Object itemType = model.getValueAt(row, 3);
        Object itemTitle = model.getValueAt(row, 2);
        Object untilObj = model.getValueAt(row, 4);
        if (!(itemType instanceof String)) 
        	return;
        MediaType type = MediaType.valueOf((String)itemType);
        LocalDate until = (untilObj instanceof LocalDate) ? (LocalDate)untilObj : null;

        // Find the first hold that matches member+item+until
        for (Hold h : LibraryData.HOLDS){
            String candidateTitle = itemTitle(type, h.getMediaId());
            if ((memberName+"").contains("#"+h.getMemberId()+")") &&
                candidateTitle.equals(itemTitle) &&
                h.getMediaType()==type &&
                ((h.getHoldUntil()==null && until==null) || (h.getHoldUntil()!=null && h.getHoldUntil().equals(until)))
            ){
                int confirm = JOptionPane.showConfirmDialog(this, "Cancel hold for "+candidateTitle+"?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION){
                    LibraryData.HOLDS.remove(h);
                    reload();
                }
                return;
            }
        }
    }
}
