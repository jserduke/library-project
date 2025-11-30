package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import client.ResponseHandler;
import message.Message;
import message.Status;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AdminPortalFrame extends JFrame {
    private static final long serialVersionUID = 1L;
	// private final User currentUser;

    public AdminPortalFrame(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
        // this.currentUser = user;
        setTitle("Admin Portal — Manage Inventory");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("  Admin Portal — Manage Inventory");
        Theme.styleHeaderBar(top, title);
        top.add(title, BorderLayout.WEST);

        JButton btnHoldsFees = new JButton("Holds & Fees");
        JButton btnLogout = new JButton("Logout");

        Theme.styleButton(btnHoldsFees, Theme.ACCENT_PURPLE);
        Theme.styleButton(btnLogout, Theme.ACCENT_ORANGE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        right.setBackground(Theme.PRIMARY);
        right.add(btnHoldsFees);
        right.add(btnLogout);

        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);


        btnHoldsFees.addActionListener(e -> {
            AdminHoldsAndFeesDialog dialog = new AdminHoldsAndFeesDialog(this, requestWriter, responseHandler);
            dialog.setVisible(true);
        });
        
        btnLogout.addActionListener(e -> {
        	responseHandler.setOldFrame(this);
        	Message logoutMessage = new Message(message.Type.REQUEST, -1, message.Action.LOGOUT, Status.PENDING, null);
        	responseHandler.setRequestIdExpected(logoutMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(logoutMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        
        add(new ManageInventoryPanel(requestWriter, responseHandler, info), BorderLayout.CENTER);
    }

    /*
    public User getCurrentUser() {
		return currentUser;
	}
	*/

	// Embedded manage-inventory panel
    public static class ManageInventoryPanel extends JPanel {
        private static final long serialVersionUID = 1L;

		private JTabbedPane tabs = new JTabbedPane();

        private DefaultTableModel booksModel = new DefaultTableModel(new Object[]{
            "ID","ISBN","Title","Author","Publisher","Genre","Total Qty","Available"
        }, 0) { 
        	public boolean isCellEditable(int r,int c){
        		return false;
        	} 
        };
        private JTable booksTable = new JTable(booksModel);

        private DefaultTableModel dvdsModel = new DefaultTableModel(new Object[]{
            "ID","Title","Rating","Runtime (min)","Total Qty","Available"
        }, 0) { 
        	public boolean isCellEditable(int r,int c){
        		return false;
        	} 
        };
        private JTable dvdsTable = new JTable(dvdsModel);

        private DefaultTableModel gamesModel = new DefaultTableModel(new Object[]{
            "ID","Title","Rating","Players","Game Length (min)","Total Qty","Available"
        }, 0) { 
        	public boolean isCellEditable(int r,int c){
        		return false;
        	} 
        };
        private JTable gamesTable = new JTable(gamesModel);

        public ManageInventoryPanel(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
            setLayout(new BorderLayout());

            tabs.add("Books", buildBooks(requestWriter, responseHandler));
            tabs.add("DVDs", buildDvds(requestWriter, responseHandler));
            tabs.add("Board Games", buildGames(requestWriter, responseHandler));
            add(tabs, BorderLayout.CENTER);
            reloadAll(info);
        }

        private JPanel buildBooks(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Theme.SURFACE);
            JScrollPane sc = new JScrollPane(booksTable);
            sc.getViewport().setBackground(Theme.SURFACE);
            p.add(sc, BorderLayout.CENTER);
            Theme.styleTable(booksTable);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btns.setBackground(Theme.SURFACE);
            JButton add = new JButton("Add");
            JButton edit = new JButton("Edit");
            JButton del = new JButton("Delete");
            Theme.styleButton(add, Theme.ACCENT_GREEN);
            Theme.styleButton(edit, Theme.ACCENT_BLUE);
            Theme.styleButton(del, Theme.ACCENT_ORANGE);
            btns.add(add); btns.add(edit); btns.add(del);
            p.add(btns, BorderLayout.SOUTH);
            add.addActionListener(e -> addBook(requestWriter, responseHandler));
            edit.addActionListener(e -> editBook(requestWriter, responseHandler));
            del.addActionListener(e -> deleteBook(requestWriter, responseHandler));
            return p;
        }
        private JPanel buildDvds(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Theme.SURFACE);
            JScrollPane sc = new JScrollPane(dvdsTable);
            sc.getViewport().setBackground(Theme.SURFACE);
            p.add(sc, BorderLayout.CENTER);
            Theme.styleTable(dvdsTable);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btns.setBackground(Theme.SURFACE);
            JButton add = new JButton("Add");
            JButton edit = new JButton("Edit");
            JButton del = new JButton("Delete");
            Theme.styleButton(add, Theme.ACCENT_GREEN);
            Theme.styleButton(edit, Theme.ACCENT_BLUE);
            Theme.styleButton(del, Theme.ACCENT_ORANGE);
            btns.add(add); btns.add(edit); btns.add(del);
            p.add(btns, BorderLayout.SOUTH);
            add.addActionListener(e -> addDvd(requestWriter, responseHandler));
            edit.addActionListener(e -> editDvd(requestWriter, responseHandler));
            del.addActionListener(e -> deleteDvd(requestWriter, responseHandler));
            return p;
        }
        private JPanel buildGames(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Theme.SURFACE);
            JScrollPane sc = new JScrollPane(gamesTable);
            sc.getViewport().setBackground(Theme.SURFACE);
            p.add(sc, BorderLayout.CENTER);
            Theme.styleTable(gamesTable);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btns.setBackground(Theme.SURFACE);
            JButton add = new JButton("Add");
            JButton edit = new JButton("Edit");
            JButton del = new JButton("Delete");
            Theme.styleButton(add, Theme.ACCENT_GREEN);
            Theme.styleButton(edit, Theme.ACCENT_BLUE);
            Theme.styleButton(del, Theme.ACCENT_ORANGE);
            btns.add(add); btns.add(edit); btns.add(del);
            p.add(btns, BorderLayout.SOUTH);
            add.addActionListener(e -> addGame(requestWriter, responseHandler));
            edit.addActionListener(e -> editGame(requestWriter, responseHandler));
            del.addActionListener(e -> deleteGame(requestWriter, responseHandler));
            return p;
        }

        public void reloadAll(ArrayList<String> info) {
        	System.out.println(info);
        	booksModel.setRowCount(0);
        	dvdsModel.setRowCount(0);
        	gamesModel.setRowCount(0);
        	for (int i = 0; i < info.size(); ) {
        		System.out.println("i: " + i);
        		if (info.get(i).equals("BOOK")) {
        			i += 1;
        			booksModel.addRow(new Object[] {
        				info.get(i++),
        				info.get(i++),
        				info.get(i++),
        				info.get(i++),
        				info.get(i++),
        				info.get(i++),
        				info.get(i++),
        				info.get(i++)
        			});
        			continue;
        		} else if (info.get(i).equals("DVD")) {
        			i += 1;
        			dvdsModel.addRow(new Object[] {
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++)
            		});
        			continue;
        		} else if (info.get(i).equals("BOARD_GAME")) {
        			i += 1;
        			gamesModel.addRow(new Object[] {
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++),
            			info.get(i++)
            		});
        			continue;
            	} else {
            		System.out.println("Media of unknown type encountered");
            	}
        	}
        	/*
            booksModel.setRowCount(0);
            for (Book b : LibraryData.BOOKS) {
                booksModel.addRow(new Object[]{
                    b.getId(), 
                    b.getIsbn(), 
                    b.getTitle(), 
                    b.getAuthor(), 
                    b.getPublisher(), 
                    b.getGenre(),
                    b.getTotalQuantity(), 
                    LibraryData.availableCountForBook(b.getId())
                });
            }
            dvdsModel.setRowCount(0);
            for (Dvd d : LibraryData.DVDS) {
                dvdsModel.addRow(new Object[]{
                    d.getId(), 
                    d.getTitle(),
                    d.getRating(), 
                    d.getRuntimeMinutes(),
                    d.getTotalQuantity(), 
                    LibraryData.availableCountForDvd(d.getId())
                });
            }
            gamesModel.setRowCount(0);
            for (BoardGame g : LibraryData.BOARD_GAMES) {
                gamesModel.addRow(new Object[]{
                    g.getId(),
                    g.getTitle(),
                    g.getRating(),
                    g.getPlayerCount(),
                    g.getGameLengthMinutes(),
                    g.getTotalQuantity(),
                    LibraryData.availableCountForBoardGame(g.getId())
                });
            }
            */
        }

        private void addBook(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JTextField isbn = new JTextField();
            JTextField title = new JTextField();
            JTextField author = new JTextField();
            JTextField publisher = new JTextField();
            JTextField genre = new JTextField();
            JTextField ddNumber = new JTextField();
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "ISBN:", isbn, "Title:", title, "Author:", author, "Publisher:", publisher, "Genre:", genre, "Dewey decimal #:", ddNumber, "Total Qty:", qty
            }, "Add Book", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> newBookInfo = new ArrayList<String>();
            	Message addBookMessage = new Message(message.Type.REQUEST, -1, message.Action.ADD_BOOK, Status.PENDING, newBookInfo);
            	newBookInfo.add(isbn.getText().trim());
            	newBookInfo.add(title.getText().trim());
            	newBookInfo.add(author.getText().trim());
            	newBookInfo.add(publisher.getText().trim());
            	newBookInfo.add(genre.getText().trim());
            	newBookInfo.add("" + (Integer) qty.getValue());
            	newBookInfo.add(ddNumber.getText().trim());
                responseHandler.setRequestIdExpected(addBookMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(addBookMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            }
        }
        private void editBook(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
        	// /*
            int row = booksTable.getSelectedRow(); 
            if (row < 0) {
            	return;
            }
            int idVal = Integer.parseInt((String) booksModel.getValueAt(row, 0));
            String isbnVal = (String) booksModel.getValueAt(row, 1);
            String titleVal = (String) booksModel.getValueAt(row, 2);
            String authorVal = (String) booksModel.getValueAt(row, 3);
            String publisherVal = (String) booksModel.getValueAt(row, 4);
            String genreVal = (String) booksModel.getValueAt(row, 5);
            int totalQuantVal = Integer.parseInt((String) booksModel.getValueAt(row, 6));
            
            JTextField isbn = new JTextField(isbnVal);
            JTextField title = new JTextField(titleVal);
            JTextField author = new JTextField(authorVal);
            JTextField publisher = new JTextField(publisherVal);
            JTextField genre = new JTextField(genreVal);
            JSpinner qtyTotal = new JSpinner(new SpinnerNumberModel(totalQuantVal,1,999,1));
            JSpinner qtyAvail = new JSpinner(new SpinnerNumberModel(totalQuantVal,1,999,1));
            // TODO: add quantity available?
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "ISBN:", isbn, "Title:", title, "Author:", author, "Publisher:", publisher, "Genre:", genre, "Total Qty:", qtyTotal, "Avail. Qty:", qtyAvail
            }, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> editBookInfo = new ArrayList<String>();
            	Message editBookMessage = new Message(message.Type.REQUEST, -1, message.Action.EDIT_BOOK, Status.PENDING, editBookInfo);
            	editBookInfo.add("" + idVal);
            	editBookInfo.add(isbn.getText().trim());
            	editBookInfo.add(title.getText().trim());
            	editBookInfo.add(author.getText().trim());
            	editBookInfo.add(publisher.getText().trim());
            	editBookInfo.add(genre.getText().trim());
            	editBookInfo.add("" + (Integer) qtyTotal.getValue());
            	editBookInfo.add("" + (Integer) qtyAvail.getValue());
                responseHandler.setRequestIdExpected(editBookMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(editBookMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
                // reloadAll(null); // TODO: fix later
            }
            // */
        }
        private void deleteBook(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            int row = booksTable.getSelectedRow(); 
            if (row < 0) {
            	return;
            }
            String id = (String) booksModel.getValueAt(row, 0);
        	ArrayList<String> deleteBookInfo = new ArrayList<String>();
        	Message deleteBookMessage = new Message(message.Type.REQUEST, -1, message.Action.DELETE_BOOK, Status.PENDING, deleteBookInfo);
        	deleteBookInfo.add(id);
            responseHandler.setRequestIdExpected(deleteBookMessage.getId());
            responseHandler.setOldPanel(this);
            try {
    			requestWriter.writeObject(deleteBookMessage);
    		} catch (IOException er) {
    			// TODO Auto-generated catch block
    			er.printStackTrace();
    		}
        }

        private void addDvd(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JTextField title = new JTextField();
            JTextField publisher = new JTextField();
            JTextField genre = new JTextField();
            JTextField rating = new JTextField();
            JSpinner runtime = new JSpinner(new SpinnerNumberModel(90,1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Publisher:", publisher, "Genre:", genre, "Rating:", rating, "Runtime (min):", runtime, "Total Qty:", qty
            }, "Add DVD", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> newDvdInfo = new ArrayList<String>();
            	Message addDvdMessage = new Message(message.Type.REQUEST, -1, message.Action.ADD_DVD, Status.PENDING, newDvdInfo);
            	newDvdInfo.add(title.getText().trim());
            	newDvdInfo.add(publisher.getText().trim());
            	newDvdInfo.add(genre.getText().trim());
            	newDvdInfo.add("" + (Integer) qty.getValue());
            	newDvdInfo.add(rating.getText().trim());
            	newDvdInfo.add("" + (Integer) runtime.getValue());
                responseHandler.setRequestIdExpected(addDvdMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(addDvdMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            }
        }
        private void editDvd(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            int row = dvdsTable.getSelectedRow(); 
            if (row < 0) {
            	return;
            }
            int idVal = Integer.parseInt((String) dvdsModel.getValueAt(row, 0));
            String titleVal = (String) dvdsModel.getValueAt(row, 1);
            String ratingVal = (String) dvdsModel.getValueAt(row, 2);
            int runtimeVal = Integer.parseInt((String) dvdsModel.getValueAt(row, 3));
            int quantTotal = Integer.parseInt((String) dvdsModel.getValueAt(row, 4));
            int quantAvail = Integer.parseInt((String) dvdsModel.getValueAt(row, 5));
            
            JTextField title = new JTextField(titleVal);
            JTextField rating = new JTextField(ratingVal);
            JSpinner runtime = new JSpinner(new SpinnerNumberModel(runtimeVal,1,10000,1));
            JSpinner qtyTotal = new JSpinner(new SpinnerNumberModel(quantTotal,1,999,1));
            JSpinner qtyAvail = new JSpinner(new SpinnerNumberModel(quantAvail,1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Runtime (min):", runtime, "Total Qty:", qtyTotal, "Avail. Qty:", qtyAvail
            }, "Edit DVD", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> editDvdInfo = new ArrayList<String>();
            	Message editDvdMessage = new Message(message.Type.REQUEST, -1, message.Action.EDIT_DVD, Status.PENDING, editDvdInfo);
            	editDvdInfo.add("" + idVal);
            	editDvdInfo.add(title.getText().trim());
            	editDvdInfo.add(rating.getText().trim());
            	editDvdInfo.add("" + runtime.getValue());
            	editDvdInfo.add("" + qtyTotal.getValue());
            	editDvdInfo.add("" + qtyAvail.getValue());
                responseHandler.setRequestIdExpected(editDvdMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(editDvdMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            }
        }
        private void deleteDvd(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            int row = dvdsTable.getSelectedRow();
            if (row<0) {
            	return;
            }
            String id = (String) dvdsModel.getValueAt(row, 0);
        	ArrayList<String> deleteDvdInfo = new ArrayList<String>();
        	Message deleteDvdMessage = new Message(message.Type.REQUEST, -1, message.Action.DELETE_DVD, Status.PENDING, deleteDvdInfo);
        	deleteDvdInfo.add(id);
            responseHandler.setRequestIdExpected(deleteDvdMessage.getId());
            responseHandler.setOldPanel(this);
            try {
    			requestWriter.writeObject(deleteDvdMessage);
    		} catch (IOException er) {
    			// TODO Auto-generated catch block
    			er.printStackTrace();
    		}
        }

        private void addGame(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            JTextField title = new JTextField();
            JTextField genre = new JTextField();
            JTextField publisher = new JTextField();
            JTextField rating = new JTextField();
            JTextField minPlayers = new JTextField();
            JTextField maxPlayers = new JTextField();
            JSpinner length = new JSpinner(new SpinnerNumberModel(60,1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Genre:", genre, "Publisher:", publisher, "Rating:", rating, "Min. Players:", minPlayers, "Max. Players:", maxPlayers, "Game Length (min):", length, "Total Qty:", qty
            }, "Add Board Game", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> newGameInfo = new ArrayList<String>();
            	Message addGameMessage = new Message(message.Type.REQUEST, -1, message.Action.ADD_GAME, Status.PENDING, newGameInfo);
            	newGameInfo.add(title.getText().trim());
            	newGameInfo.add(genre.getText().trim());
            	newGameInfo.add(publisher.getText().trim());
            	newGameInfo.add(rating.getText().trim());
            	newGameInfo.add(minPlayers.getText().trim());
            	newGameInfo.add(maxPlayers.getText().trim());
            	newGameInfo.add("" + (Integer) length.getValue());
            	newGameInfo.add("" + (Integer) qty.getValue());
                responseHandler.setRequestIdExpected(addGameMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(addGameMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            }
        }
        private void editGame(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            int row = gamesTable.getSelectedRow(); 
            if (row < 0) {
            	return;
            }
            int id = Integer.parseInt((String) gamesModel.getValueAt(row, 0));
            String titleVal = (String) gamesModel.getValueAt(row, 1);
            String ratingVal = (String) gamesModel.getValueAt(row, 2);
            String[] players = ((String) gamesModel.getValueAt(row, 3)).split("-");
            String minPlayersVal = players[0];
            String maxPlayersVal = players[1];
            int lengthVal = Integer.parseInt((String) gamesModel.getValueAt(row, 4));
            int totalQtyVal = Integer.parseInt((String) gamesModel.getValueAt(row, 5));
            int availQtyVal = Integer.parseInt((String) gamesModel.getValueAt(row, 6));
            
            JTextField title = new JTextField(titleVal);
            JTextField rating = new JTextField(ratingVal);
            JTextField minPlayers = new JTextField(minPlayersVal);
            JTextField maxPlayers = new JTextField(maxPlayersVal);
            JSpinner length = new JSpinner(new SpinnerNumberModel(lengthVal, 1, 10000, 1));
            JSpinner totalQty = new JSpinner(new SpinnerNumberModel(totalQtyVal, 1, 999, 1));
            JSpinner availQty = new JSpinner(new SpinnerNumberModel(availQtyVal, 1, 999, 1));
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Min. Players:", minPlayers, "Max. Players:", maxPlayers, "Game Length (min):", length, "Total Qty:", totalQty, "Avail. Qty:", availQty
            }, "Edit Board Game", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> editGameInfo = new ArrayList<String>();
            	Message editGameMessage = new Message(message.Type.REQUEST, -1, message.Action.EDIT_GAME, Status.PENDING, editGameInfo);
            	editGameInfo.add("" + id);
            	editGameInfo.add(title.getText().trim());
            	editGameInfo.add(rating.getText().trim());
            	editGameInfo.add(minPlayers.getText().trim());
            	editGameInfo.add(maxPlayers.getText().trim());
            	editGameInfo.add("" + length.getValue());
            	editGameInfo.add("" + totalQty.getValue());
            	editGameInfo.add("" + availQty.getValue());
                responseHandler.setRequestIdExpected(editGameMessage.getId());
                responseHandler.setOldPanel(this);
                try {
        			requestWriter.writeObject(editGameMessage);
        		} catch (IOException er) {
        			// TODO Auto-generated catch block
        			er.printStackTrace();
        		}
            }
        }
        private void deleteGame(ObjectOutputStream requestWriter, ResponseHandler responseHandler) {
            int row = gamesTable.getSelectedRow();
            if (row < 0) { 
            	return;
            }
            String id = (String) gamesModel.getValueAt(row, 0);
        	ArrayList<String> deleteGameInfo = new ArrayList<String>();
        	Message deleteGameMessage = new Message(message.Type.REQUEST, -1, message.Action.DELETE_GAME, Status.PENDING, deleteGameInfo);
        	deleteGameInfo.add(id);
            responseHandler.setRequestIdExpected(deleteGameMessage.getId());
            responseHandler.setOldPanel(this);
            try {
    			requestWriter.writeObject(deleteGameMessage);
    		} catch (IOException er) {
    			// TODO Auto-generated catch block
    			er.printStackTrace();
    		}
        }
    }
}