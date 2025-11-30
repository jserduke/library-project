package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import client.ResponseHandler;
import message.*;

public class MemberPortalFrame extends JFrame {
	private HoldsPanel holdsPanel;
    private static final long serialVersionUID = 1L;

    private final int memberId;
    private final ObjectOutputStream requestWriter;
	private final ResponseHandler responseHandler; 

    private JTextField txtSearch = new JTextField(18);
    private JComboBox<String> cbType = new JComboBox<>(new String[]{"All", "Books", "DVDs", "Board Games"});
    private DefaultTableModel catalogModel = new DefaultTableModel(new Object[]{
    		"Type","Id","Title","Author / Rating","Publisher / Studio / Running Time","Genre","Total Qty","Available"
    }, 0) { 
    	public boolean isCellEditable(int r,int c){
    		return false;
    	} 
    };
    private JTable catalogTable = new JTable(catalogModel);

    private DefaultTableModel loansModel = new DefaultTableModel(new Object[]{
            "Loan ID","Type","Media ID","Title","Checkout","Due","Returned"
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
    	this.memberId = Integer.parseInt(info.get(1));
    	
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

        reloadCatalog(info, 2, true);
        reloadLoans(info, 2 + Integer.parseInt(info.get(1)) * 8);
    }
    
    public ObjectOutputStream getRequestWriter() {
    	return requestWriter;
    }

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

        btnSearch.addActionListener(e -> {
        	// reloadCatalog(info, 1);
        	ArrayList<String> queryInfo = new ArrayList<String>();
        	queryInfo.add((String) cbType.getSelectedItem());
        	queryInfo.add(txtSearch.getText().trim().toLowerCase());
        	Message searchMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_SEARCH_MEMBER, Status.PENDING, queryInfo);
        	responseHandler.setRequestIdExpected(searchMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(searchMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        txtSearch.addActionListener(e -> {
        	// reloadCatalog(info, 1, false);
        	ArrayList<String> queryInfo = new ArrayList<String>();
        	queryInfo.add((String) cbType.getSelectedItem());
        	queryInfo.add(txtSearch.getText().trim().toLowerCase());
        	Message searchMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_SEARCH_MEMBER, Status.PENDING, queryInfo);
        	responseHandler.setRequestIdExpected(searchMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(searchMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        cbType.addActionListener(e -> {
        	ArrayList<String> queryInfo = new ArrayList<String>();
        	queryInfo.add((String) cbType.getSelectedItem());
        	queryInfo.add(txtSearch.getText().trim().toLowerCase());
        	Message searchMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_SEARCH_MEMBER, Status.PENDING, queryInfo);
        	responseHandler.setRequestIdExpected(searchMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(searchMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }); // TODO: change
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
	
    public void openHoldsAndFees() {
    	Message msg = new Message(0, message.Type.REQUEST , -1, message.Action.GET_HOLDS, Status.PENDING, null);
    	responseHandler.setRequestIdExpected(msg.getId());
    	responseHandler.setOldFrame(this);
    	
    	try {
    		requestWriter.writeObject(msg);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
	}
    public void reloadCatalog(ArrayList<String> info, int invStart, boolean isInit) {
        String q = txtSearch.getText().trim();
        String type = cbType.getSelectedItem().toString();
        catalogModel.setRowCount(0);
        
        if (isInit) {
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
        } else {
	    	for (int i = 0; i < (info.size() - invStart) / 8; i += 1) {
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
        }
    }

    public void reloadLoans(ArrayList<String> info, int loansStart) {
        loansModel.setRowCount(0);
        int fieldsPerLoan = 7;
        int total = (info.size() - loansStart) / fieldsPerLoan;

        for (int i = 0; i < total; i++) {
        	int base = loansStart + i * fieldsPerLoan;
        	
	        loansModel.addRow(new Object[] {
		        info.get(base + 0),
		        info.get(base + 1),
		        info.get(base + 2),
		        info.get(base + 3),
		        info.get(base + 4),
		        info.get(base + 5),
		        info.get(base + 6),
	        });
        }
    }
    
    public void reloadHolds(ArrayList<String> info, int holdsStart) {
    	if (holdsPanel != null) {
    		holdsPanel.reload(info);
    	}
    }

    public void checkoutSelected() {
    	int row = catalogTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a media item."); 
        	return;
        }
        
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

    public void holdSelected() {
    	
        int row = catalogTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a media item."); 
        	return; 
        }
        
        int mediaId = Integer.parseInt(catalogModel.getValueAt(row, 1).toString());
        long untilMillis = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 7);
        
        ArrayList<String> info = new ArrayList<>();
        info.add(Integer.toString(mediaId));
        info.add(Long.toString(untilMillis));
        Message holdMessage = new Message(0, message.Type.REQUEST, -1, message.Action.PLACE_HOLD, Status.PENDING, info);
        responseHandler.setRequestIdExpected(holdMessage.getId());
        responseHandler.setOldFrame(this);
        
        try {
        	requestWriter.writeObject(holdMessage);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }

    public void returnSelectedLoan() {
    	
        int row = loansTable.getSelectedRow();
        if (row < 0) { 
        	JOptionPane.showMessageDialog(this, "Select a media to return."); 
        	return; 
        }
        
        int mediaId = Integer.parseInt(loansModel.getValueAt(row, 2).toString());
        
        ArrayList<String> info = new ArrayList<>();
        info.add(Integer.toString(mediaId));
        
        Message msg = new Message(0, message.Type.REQUEST, -1, message.Action.RETURN, Status.PENDING, info);
        
        responseHandler.setRequestIdExpected(msg.getId());
        responseHandler.setOldFrame(this);
        
        try {
        	requestWriter.writeObject(msg);
        } catch(IOException e) {
        	e.printStackTrace();
        }
    }

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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage());
            }
        }
    }
}
