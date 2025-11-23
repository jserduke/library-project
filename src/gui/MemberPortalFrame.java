package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import client.ResponseHandler;
import message.*;

public class MemberPortalFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final ObjectOutputStream requestWriter;
	private final ResponseHandler responseHandler;
	// private final User user;
    // private final Member member;

    private JTextField txtSearch = new JTextField(18);
    private JComboBox<String> cbType = new JComboBox<>(new String[]{"All", "Books", "DVDs", "Board Games"});
    private DefaultTableModel catalogModel = new DefaultTableModel(new Object[]{
    		"Type","Id","Title","Author / Rating","Publisher / Studio / Length","Genre","Total Qty","Available"
    }, 0) { 
    	public boolean isCellEditable(int r,int c){
    		return false;
    	} 
    };
    private JTable catalogTable = new JTable(catalogModel);

    private DefaultTableModel loansModel = new DefaultTableModel(new Object[]{
            "Loan ID","Type","Media ID","Title","Checkout","Due","Grace (days)","Returned"
    }, 0) { 
    	public boolean isCellEditable(int r,int c){
    		return false;
    	} 
    };
    private JTable loansTable = new JTable(loansModel);

    public MemberPortalFrame(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
        // this.user = user;
        // this.member = resolveMember(user);
    	
    	this.requestWriter = requestWriter;
    	this.responseHandler = responseHandler;
        setTitle("Member Portal — " + info.getFirst()); // TODO: replace with username from message
        setSize(1100, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("  Member Portal");
        Theme.styleHeaderBar(top, title);
        top.add(title, BorderLayout.WEST);
        JButton btnLogout = new JButton("Logout");
        Theme.styleButton(btnLogout, Theme.ACCENT_ORANGE);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        right.setBackground(Theme.PRIMARY);
        right.add(btnLogout);
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        btnLogout.addActionListener(e -> {
        	responseHandler.setOldFrame(this);
        	Message logoutMessage = new Message(0, message.Type.REQUEST, -1, message.Action.LOGOUT, Status.PENDING, null);
        	responseHandler.setRequestIdExpected(logoutMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(logoutMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });

        // Split
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.55);
        split.setTopComponent(buildCatalogPanel(requestWriter, responseHandler, info));
        split.setBottomComponent(buildLoansPanel());
        add(split, BorderLayout.CENTER);

//		  Commenting this out for now
//        reloadCatalog(info, 2);
//        reloadLoans(info, 2 + Integer.parseInt(info.get(1)) * 8);
//        ArrayList<String> dummy = new ArrayList<>();
//		Message dashboard = new Message(0, message.Type.REQUEST, -1, message.Action.GET_DASHBOARD, Status.PENDING, dummy);
//		responseHandler.setRequestIdExpected(dashboard.getId());
//		responseHandler.setOldFrame(this);
//		
//		try {
//			requestWriter.writeObject(dashboard);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		setVisible(true);
    }

    /*
    private Member resolveMember(User u) {
        if (u.getMemberId() != null) {
            for (Member m : LibraryData.MEMBERS) 
            	if (m.getId() == u.getMemberId()) 
            		return m;
        }
        Member m = LibraryData.findMemberByEmail(u.getUsername());
        if (m != null) 
        	return m;
        Member nm = new Member(LibraryData.nextMemberId(), u.getUsername(), "", LocalDate.of(2000,1,1), "", u.getUsername());
        LibraryData.MEMBERS.add(nm);
        u.setMemberId(nm.getId());
        return nm;
    }
    */

    private JPanel buildCatalogPanel(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.SURFACE);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        
        Theme.styleControlStrip(search);
        
        search.add(new JLabel("Search:"));
        search.add(txtSearch);
        search.add(new JLabel("Type:"));
        search.add(cbType);
        
        JButton btnSearch = new JButton("Search");
        JButton btnCheckout = new JButton("Checkout Selected");
        JButton btnHold = new JButton("Place Hold");
        JButton btnEdit = new JButton("Edit Account");
        JButton btnHoldsFees = new JButton("My Holds & Fees");

        Theme.styleButton(btnSearch, Theme.DARK_BUTTON);
        Theme.styleButton(btnCheckout, Theme.ACCENT_BLUE);
        Theme.styleButton(btnHold, Theme.ACCENT_PURPLE);
        Theme.styleButton(btnEdit, Theme.ACCENT_GREEN);
        Theme.styleButton(btnHoldsFees, Theme.ACCENT_ORANGE);

        search.add(btnSearch);
        search.add(btnCheckout);
        search.add(btnHold);
        search.add(btnEdit);
        search.add(btnHoldsFees);

        panel.add(search, BorderLayout.NORTH);

        JScrollPane sc = new JScrollPane(catalogTable);
        sc.getViewport().setBackground(Theme.SURFACE);
        panel.add(sc, BorderLayout.CENTER);
        
        Theme.styleTable(catalogTable);

        btnSearch.addActionListener(e -> reloadCatalog(info, 1));
        txtSearch.addActionListener(e -> reloadCatalog(info, 1));
        cbType.addActionListener(e -> reloadCatalog(info, 1));
        btnCheckout.addActionListener(e -> checkoutSelected());
        btnHold.addActionListener(e -> holdSelected());
        btnEdit.addActionListener(e -> {
            Message getProfileMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_PROFILE, Status.PENDING, null);
            responseHandler.setRequestIdExpected(getProfileMessage.getId());
            responseHandler.setOldFrame(this);
            try {
    			requestWriter.writeObject(getProfileMessage);
    		} catch (IOException er) {
    			// TODO Auto-generated catch block
    			er.printStackTrace();
    		}
        	// editAccount();
        });
        btnHoldsFees.addActionListener(e -> openHoldsAndFees());
        return panel;

    }


	private JPanel buildLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.SURFACE);
        JLabel lbl = new JLabel("  My Loans");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        panel.add(lbl, BorderLayout.NORTH);
        JScrollPane sc = new JScrollPane(loansTable);
        sc.getViewport().setBackground(Theme.SURFACE);
        panel.add(sc, BorderLayout.CENTER);
        Theme.styleTable(loansTable);
        JButton btnReturn = new JButton("Return Selected Loan");
        Theme.styleButton(btnReturn, Theme.ACCENT_ORANGE);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Theme.SURFACE);
        bottom.add(btnReturn);
        panel.add(bottom, BorderLayout.SOUTH);
        btnReturn.addActionListener(e -> returnSelectedLoan());
        return panel;
    }
	
    private void openHoldsAndFees() {
    	/*
    	MemberAccountDialog dialog = new MemberAccountDialog(this, member);
    	dialog.setVisible(true);
    	*/

	}
    public void reloadCatalog(ArrayList<String> info, int invStart) {
        String q = txtSearch.getText().trim();
        String type = cbType.getSelectedItem().toString();
        catalogModel.setRowCount(0);
        
    	for (int i = 0; i < Integer.parseInt(info.get(1)); i += 1) {
    		catalogModel.addRow(new Object[] {
    				info.get(invStart + i * 8 + 0),
    				info.get(invStart + i * 8 + 1),
    				info.get(invStart + i * 8 + 2),
    				info.get(invStart + i * 8 + 3),
    				info.get(invStart + i * 8 + 4),
    				info.get(invStart + i * 8 + 5),
    				info.get(invStart + i * 8 + 6),
    				info.get(invStart + i * 8 + 7),
    		});
    	}

    	/*
        if ("All".equals(type) || "Books".equals(type)) {
            for (Book b : (q.isEmpty()? LibraryData.BOOKS : LibraryData.searchBooks(q))) {
                catalogModel.addRow(new Object[]{
                    "Book", b.getId(), b.getTitle(), b.getAuthor(), b.getPublisher(), b.getGenre(),
                    b.getTotalQuantity(), LibraryData.availableCountForBook(b.getId())
                });
            }
        }
        if ("All".equals(type) || "DVDs".equals(type)) {
            for (Dvd d : (q.isEmpty()? LibraryData.DVDS : LibraryData.searchDvds(q))) {
                catalogModel.addRow(new Object[]{
                    "DVD", d.getId(), d.getTitle(), d.getRating(), d.getRuntimeMinutes()+" min", "",
                    d.getTotalQuantity(), LibraryData.availableCountForDvd(d.getId())
                });
            }
        }
        if ("All".equals(type) || "Board Games".equals(type)) {
            for (BoardGame g : (q.isEmpty()? LibraryData.BOARD_GAMES : LibraryData.searchBoardGames(q))) {
                catalogModel.addRow(new Object[]{
                    "Board Game", g.getId(), g.getTitle(), g.getRating(), g.getPlayerCount(), g.getGameLengthMinutes()+" min",
                    g.getTotalQuantity(), LibraryData.availableCountForBoardGame(g.getId())
                });
            }
        }
        */
    }

    public void reloadLoans(ArrayList<String> info, int loansStart) {
        loansModel.setRowCount(0);
        for (int i = 0; i < (info.size() - loansStart) / 8; i += 1) {
	        loansModel.addRow(new Object[] {
	        	info.get(loansStart + i * 8 + 0),
	        	info.get(loansStart + i * 8 + 1),
	        	info.get(loansStart + i * 8 + 2),
	        	info.get(loansStart + i * 8 + 3),
	        	info.get(loansStart + i * 8 + 4),
	        	info.get(loansStart + i * 8 + 5),
	        	info.get(loansStart + i * 8 + 6),
	        	info.get(loansStart + i * 8 + 7),
	        });
        }
        /*
        for (Loan l : LibraryData.loansForMember(member.getId())) {
            String title = getTitle(l.getMediaType(), l.getMediaId());
            loansModel.addRow(new Object[]{
                l.getId(), l.getMediaType().name(), l.getMediaId(), title,
                l.getCheckoutDate(), l.getDueDate(), l.getGracePeriodDays(),
                (l.getReturnDate()==null? "" : l.getReturnDate().toString())
            });
        }
        */
    }

    // DON'T THINK WE NEED?
    /*
    private String getTitle(MediaType type, int id) {
        switch (type) {
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
        return "";
    }
    */

    // WILL GET WORKING LATER
    private void checkoutSelected() {
    	
        int row = catalogTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a media item."); 
        	return;
        }
        
        /*
        String typeStr = catalogModel.getValueAt(row, 0).toString();
        int id = (Integer) catalogModel.getValueAt(row, 1);
        
        MediaType type = "Book".equals(typeStr) ? MediaType.BOOK : ("DVD".equals(typeStr)? MediaType.DVD : MediaType.BOARD_GAME);
        Loan loan = LibraryData.checkout(member.getId(), type, id);
        if (loan == null) {
            JOptionPane.showMessageDialog(this, "No copies available to checkout.");
        } else {
            JOptionPane.showMessageDialog(this, "Checked out. Due: " + loan.getDueDate());
        }
        reloadCatalog();
        reloadLoans();
        */
        
        int mediaId = Integer.parseInt(catalogModel.getValueAt(row, 1).toString());
        long dueMillis = System.currentTimeMillis() + (1000L*60*60*24*14);
        
        ArrayList<String> info = new ArrayList<String>();
        info.add(Integer.toString(mediaId));
        info.add(Long.toString(dueMillis));
        Message checkoutMessage = new Message(0, message.Type.REQUEST, -1, message.Action.CHECKOUT, Status.PENDING, info);
        responseHandler.setRequestIdExpected(checkoutMessage.getId());
        responseHandler.setOldFrame(this);
        
        try {
        	requestWriter.writeObject(checkoutMessage);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        
    }

    // WILL GET WORKING LATER
    private void holdSelected() {
    	/*
        int row = catalogTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a media item."); 
        	return; 
        }
        String typeStr = catalogModel.getValueAt(row, 0).toString();
        int id = (Integer) catalogModel.getValueAt(row, 1);
        MediaType type = "Book".equals(typeStr) ? MediaType.BOOK : ("DVD".equals(typeStr)? MediaType.DVD : MediaType.BOARD_GAME);

        JTextField date = new JTextField(LocalDate.now().plusDays(7).toString());
        int res = JOptionPane.showConfirmDialog(this, new Object[]{"Hold until (YYYY-MM-DD):", date}, "Place Hold", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            LocalDate holdUntil = LocalDate.parse(date.getText().trim());
            LibraryData.placeHold(member.getId(), type, id, holdUntil);
            JOptionPane.showMessageDialog(this, "Hold placed until: " + holdUntil);
        }
        */
    }

    // WILL GET WORKING LATER
    private void returnSelectedLoan() {
    	/*
        int row = loansTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a loan."); 
        	return; 
        }
        int loanId = (Integer) loansModel.getValueAt(row, 0);
        if (LibraryData.returnLoan(loanId)) {
            JOptionPane.showMessageDialog(this, "Returned.");
            reloadCatalog();
            reloadLoans();
        }
        */
    }

    // WILL GET WORKING HOPEFULLY TONIGHT
    public void editAccount(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
    	String[] names = info.getFirst().split(" ");
        JTextField first = new JTextField(names[0]);
        JTextField last = new JTextField(names[1]);
        JTextField dob = new JTextField(info.get(1));
        // JTextField phone = new JTextField(member.getPhone());
        JTextField email = new JTextField(info.get(2));
        int res = JOptionPane.showConfirmDialog(this, new Object[]{
            "First name:", first, "Last name:", last, "Birthday (YYYY-MM-DD):", dob,
            "Email:", email
        }, "Edit Account", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
            	ArrayList<String> newInfo = new ArrayList<String>();
            	Message setProfileMessage = new Message(0, message.Type.REQUEST, -1, message.Action.SET_PROFILE, Status.PENDING, newInfo);
            	newInfo.add(first.getText().trim() + " " + last.getText().trim());
            	newInfo.add(dob.getText());
            	newInfo.add(email.getText());
                responseHandler.setRequestIdExpected(setProfileMessage.getId());
                responseHandler.setOldFrame(this);
                try {
        			requestWriter.writeObject(setProfileMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            	/*
                member.setFirstName(first.getText().trim());
                member.setLastName(last.getText().trim());
                member.setBirthday(LocalDate.parse(dob.getText().trim()));
                member.setPhone(phone.getText().trim());
                member.setEmail(email.getText().trim());
                */
                // JOptionPane.showMessageDialog(this, "Account updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage());
            }
        }
    }

    // DON'T THINK WE NEED
    /*
	public User getUser() {
		return user;
	}
	*/
}
