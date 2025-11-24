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
            AdminHoldsAndFeesDialog dialog = new AdminHoldsAndFeesDialog(this);
            dialog.setVisible(true);
        });
        
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
            del.addActionListener(e -> deleteBook());
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
            add.addActionListener(e -> addDvd());
            edit.addActionListener(e -> editDvd());
            del.addActionListener(e -> deleteDvd());
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
            add.addActionListener(e -> addGame());
            edit.addActionListener(e -> editGame());
            del.addActionListener(e -> deleteGame());
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
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "ISBN:", isbn, "Title:", title, "Author:", author, "Publisher:", publisher, "Genre:", genre, "Total Qty:", qty
            }, "Add Book", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
            	ArrayList<String> newBookInfo = new ArrayList<String>();
            	Message addBookMessage = new Message(0, message.Type.REQUEST, -1, message.Action.ADD_BOOK, Status.PENDING, newBookInfo);
            	newBookInfo.add(isbn.getText().trim());
            	newBookInfo.add(title.getText().trim());
            	newBookInfo.add(author.getText().trim());
            	newBookInfo.add(publisher.getText().trim());
            	newBookInfo.add(genre.getText().trim());
            	newBookInfo.add("" + (Integer) qty.getValue());
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
            	Message editBookMessage = new Message(0, message.Type.REQUEST, -1, message.Action.EDIT_BOOK, Status.PENDING, editBookInfo);
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
        private void deleteBook() {
            int row = booksTable.getSelectedRow(); 
            if (row<0) 
            	return;
            int id = (Integer) booksModel.getValueAt(row, 0);
            LibraryData.BOOKS.removeIf(b -> b.getId()==id);
            reloadAll(null); // TODO: fix later
        }

        private void addDvd() {
            JTextField title = new JTextField();
            JTextField rating = new JTextField();
            JSpinner runtime = new JSpinner(new SpinnerNumberModel(90,1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Runtime (min):", runtime, "Total Qty:", qty
            }, "Add DVD", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                LibraryData.DVDS.add(new Dvd(LibraryData.nextDvdId(), 
                		title.getText().trim(), 
                		rating.getText().trim(),
                    (Integer)runtime.getValue(), 
                    (Integer)qty.getValue()));
                reloadAll(null); // TODO: fix later
            }
        }
        private void editDvd() {
            int row = dvdsTable.getSelectedRow(); 
            if (row<0) 
            	return;
            
            int id = (Integer) dvdsModel.getValueAt(row, 0);
            
            Dvd found = null; 
            for (Dvd d : LibraryData.DVDS) 
            	if (d.getId()==id){
            		found=d;
            		break;
            	}
            if (found==null) 
            	return;
            
            JTextField title = new JTextField(found.getTitle());
            JTextField rating = new JTextField(found.getRating());
            JSpinner runtime = new JSpinner(new SpinnerNumberModel(found.getRuntimeMinutes(),1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(found.getTotalQuantity(),1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Runtime (min):", runtime, "Total Qty:", qty
            }, "Edit DVD", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                found.setTitle(title.getText().trim());
                found.setRating(rating.getText().trim());
                found.setRuntimeMinutes((Integer)runtime.getValue());
                found.setTotalQuantity((Integer)qty.getValue());
                reloadAll(null); // TODO: fix later
            }
        }
        private void deleteDvd() {
            int row = dvdsTable.getSelectedRow(); if (row<0) 
            	return;
            int id = (Integer) dvdsModel.getValueAt(row, 0);
            LibraryData.DVDS.removeIf(d -> d.getId()==id);
            reloadAll(null); // TODO: fix later
        }

        private void addGame() {
            JTextField title = new JTextField();
            JTextField rating = new JTextField();
            JTextField players = new JTextField("2-4");
            JSpinner length = new JSpinner(new SpinnerNumberModel(60,1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(1,1,999,1));
            
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Players:", players, "Game Length (min):", length, "Total Qty:", qty
            }, "Add Board Game", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                LibraryData.BOARD_GAMES.add(new BoardGame(LibraryData.nextBoardGameId(), 
                		title.getText().trim(),
                    rating.getText().trim(),
                    players.getText().trim(), 
                    (Integer)length.getValue(),
                    (Integer)qty.getValue()));
                reloadAll(null); // TODO: fix later
            }
        }
        private void editGame() {
            int row = gamesTable.getSelectedRow(); 
            if (row<0) 
            	return;
            
            int id = (Integer) gamesModel.getValueAt(row, 0);
            
            BoardGame found = null; 
            for (BoardGame g : LibraryData.BOARD_GAMES) 
            	if (g.getId()==id){
            		found=g;
            		break;
            	}
            if (found==null) 
            	return;
            
            JTextField title = new JTextField(found.getTitle());
            JTextField rating = new JTextField(found.getRating());
            JTextField players = new JTextField(found.getPlayerCount());
            JSpinner length = new JSpinner(new SpinnerNumberModel(found.getGameLengthMinutes(),1,10000,1));
            JSpinner qty = new JSpinner(new SpinnerNumberModel(found.getTotalQuantity(),1,999,1));
            int res = JOptionPane.showConfirmDialog(this, new Object[]{
                "Title:", title, "Rating:", rating, "Players:", players, "Game Length (min):", length, "Total Qty:", qty
            }, "Edit Board Game", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                found.setTitle(title.getText().trim());
                found.setRating(rating.getText().trim());
                found.setPlayerCount(players.getText().trim());
                found.setGameLengthMinutes((Integer)length.getValue());
                found.setTotalQuantity((Integer)qty.getValue());
                reloadAll(null); // TODO: fix later
            }
        }
        private void deleteGame() {
            int row = gamesTable.getSelectedRow();
            if (row<0) 
            	return;
            int id = (Integer) gamesModel.getValueAt(row, 0);
            LibraryData.BOARD_GAMES.removeIf(g -> g.getId()==id);
            reloadAll(null); // TODO: fix later
        }
    }
}
