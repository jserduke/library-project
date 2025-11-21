package LibraryGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class OverduesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Loan ID","Member","Item","Type","Due Date","Days Late","Projected Fee"}, 0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);

    public OverduesPanel(){
        super(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        reload();
    }

    private JComponent buildTopBar(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Overdue Checkouts"));
        return p;
    }

    private JComponent buildButtons(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refresh = new JButton("Refresh");
        p.add(refresh);
        refresh.addActionListener(e -> reload());
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
        for (Loan l : LibraryData.LOANS){
            if (LoanUtil.isOverdue(l, today)){
                model.addRow(new Object[]{
                        l.getId(),
                        findMemberName(l.getMemberId()),
                        itemTitle(l.getMediaType(), l.getMediaId()),
                        l.getMediaType().name(),
                        l.getDueDate(),
                        LoanUtil.daysLate(l, today),
                        centsToMoney(LateFeeService.projectedFeeCents(l, today))
                });
            }
        }
        if (model.getRowCount()==0){
            model.addRow(new Object[]{"", "(none)", "", "", "", "", ""});
        }
    }

    private String centsToMoney(int cents){
        int dollars = cents / 100;
        int rem = Math.abs(cents % 100);
        return String.format("$%d.%02d", dollars, rem);
    }
}
