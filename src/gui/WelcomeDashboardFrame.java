package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import client.ResponseHandler;
import message.*;

public class WelcomeDashboardFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // --- Filters / search ----------------------------------------------------
    private final JTextField txtSearch = new JTextField(28);
    private final JComboBox<String> cbType =
            new JComboBox<>(new String[]{"All", "Books", "DVDs", "Board Games"});

    // --- Cards + catalog table ----------------------------------------------
    private final JPanel cardsWrap = new JPanel(new BorderLayout());
    private final JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 16, 16));

    private DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);

    private final CardLayout centerCards = new CardLayout();
    private final JPanel center = new JPanel(centerCards);

    public WelcomeDashboardFrame(ObjectOutputStream requestWriter, ResponseHandler responseHandler, ArrayList<String> info) {
        super(info.getFirst() + " — Welcome");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // --- HEADER ----------------------------------------------------------
        JPanel header = new JPanel();
        Theme.styleHeaderBar(header, new JLabel("  " + info.getFirst()));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        Theme.styleButton(btnLogin, Theme.ACCENT_BLUE);
        Theme.styleButton(btnRegister, Theme.ACCENT_GREEN);
        right.setOpaque(false);
        right.add(btnLogin);
        right.add(btnRegister);
        header.add(right, BorderLayout.EAST);

        // --- NAV (TOP) -------------------------------------------------------
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        nav.setBackground(Theme.PRIMARY);
        String[] sections = {"Books & More","My Account","Services","Calendar","How Do I?"};
        for (String s : sections) {
            JButton b = new JButton(s);
            Theme.styleButton(b, new Color(0,0,0,0));
            b.setForeground(Color.WHITE);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
            nav.add(b);
            switch (s) {
                case "Books & More" -> b.addActionListener(e -> {
                    centerCards.show(center, "CATALOG"); txtSearch.requestFocusInWindow();
                });
                // TODO: add old frame here
                case "My Account" -> b.addActionListener(e -> {
                	responseHandler.setOldFrame(this);
                	new LoginFrame(requestWriter, responseHandler, info.getFirst(), false).setVisible(true);
                });
                case "Services" -> b.addActionListener(e -> JOptionPane.showMessageDialog(this, "Services panel coming soon."));
                case "Calendar" -> b.addActionListener(e -> centerCards.show(center, "EVENTS"));
                case "How Do I?" -> b.addActionListener(e -> JOptionPane.showMessageDialog(this, "Use Register to create an account.\nLogin to place holds and manage loans."));
            }
        }
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(header);
        top.add(nav);
        add(top, BorderLayout.NORTH);

        btnLogin.addActionListener(e -> {
        	responseHandler.setOldFrame(this);
        	new LoginFrame(requestWriter, responseHandler, info.getFirst(), false).setVisible(true);
        });
        btnRegister.addActionListener(e -> {
        	responseHandler.setOldFrame(this);
        	new RegisterFrame(this, requestWriter, responseHandler, false).setVisible(true);
        });

        // --- CATALOG view ----------------------------------------------------
        JPanel catalog = new JPanel(new BorderLayout());
        Theme.applySurface(catalog);

        // Search strip + Admin entry
        JPanel searchStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Theme.styleControlStrip(searchStrip);
        searchStrip.add(new JLabel("Search:"));
        searchStrip.add(txtSearch);
        searchStrip.add(new JLabel("Type:"));
        searchStrip.add(cbType);
        JButton btnSearch = new JButton("Search");
        Theme.styleButton(btnSearch, Theme.ACCENT_BLUE);
        searchStrip.add(btnSearch);

        JButton btnAdmin = new JButton("Admin Tools");
        Theme.styleButton(btnAdmin, Theme.DARK_BUTTON);
        searchStrip.add(Box.createHorizontalStrut(12));
        searchStrip.add(btnAdmin);

        // Cards grid (TOP of catalog content)
        cardsWrap.setBorder(new javax.swing.border.EmptyBorder(16, 16, 0, 16));
        cardsGrid.setBackground(Theme.SURFACE);
        cardsWrap.add(cardsGrid, BorderLayout.CENTER);

        // Table
        table.setAutoCreateRowSorter(true);
        Theme.styleTable(table);
        JScrollPane sc = new JScrollPane(table);
        sc.getViewport().setBackground(Theme.SURFACE);

        catalog.add(searchStrip, BorderLayout.NORTH);
        catalog.add(cardsWrap, BorderLayout.CENTER);
        catalog.add(sc, BorderLayout.SOUTH);

        // Events stub
        JPanel events = new JPanel(new BorderLayout());
        Theme.applySurface(events);
        events.add(new JLabel("Events view — (cards can go here too)",
                SwingConstants.CENTER), BorderLayout.CENTER);

        center.add(catalog, "CATALOG");
        center.add(events,  "EVENTS");
        add(center, BorderLayout.CENTER);

        // Listeners
        // info.removeFirst();
        btnSearch.addActionListener(e -> {
        	ArrayList<String> queryInfo = new ArrayList<String>();
        	queryInfo.add((String) cbType.getSelectedItem());
        	queryInfo.add(txtSearch.getText().trim().toLowerCase());
        	Message searchMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_SEARCH, Status.PENDING, queryInfo);
        	responseHandler.setRequestIdExpected(searchMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(searchMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        // TODO: should be the same as btnSearch, I think
        cbType.addActionListener(e -> {
        	ArrayList<String> queryInfo = new ArrayList<String>();
        	queryInfo.add((String) cbType.getSelectedItem());
        	queryInfo.add(txtSearch.getText().trim().toLowerCase());
        	Message searchMessage = new Message(0, message.Type.REQUEST, -1, message.Action.GET_SEARCH, Status.PENDING, queryInfo);
        	responseHandler.setRequestIdExpected(searchMessage.getId());
        	responseHandler.setOldFrame(this);
        	try {
				requestWriter.writeObject(searchMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	// reloadResults(info, 1);
        });
        btnAdmin.addActionListener(e -> {
        	responseHandler.setOldFrame(this);
        	(new LoginFrame(requestWriter, responseHandler, info.getFirst(), true)).setVisible(true);
        });

        // Initial data
        setCatalogColumnsForType("All");
        reloadResults(info, 1);
    }

    // --- Helpers -------------------------------------------------------------

    private void setCatalogColumnsForType(String typeLabel) {
        String c3 = "Author / Rating / Players";
        String c4 = "ISBN / Runtime";
        String c5 = "Publisher / Studio / Length";

        if ("Books".equals(typeLabel)) {
            model.setColumnIdentifiers(new Object[]{"ID","Title","Author","ISBN","Publisher","Total Qty","Available"});
        } else if ("DVDs".equals(typeLabel)) {
            model.setColumnIdentifiers(new Object[]{"ID","Title","Rating","Runtime","Studio","Total Qty","Available"});
        } else if ("Board Games".equals(typeLabel)) {
            model.setColumnIdentifiers(new Object[]{"ID","Title","Rating","Players","Length","Total Qty","Available"});
        } else { 
            model.setColumnIdentifiers(new Object[]{"Type","ID","Title", c3, c4, c5,"Total Qty","Available"});
        }
    }

    public void reloadResults(ArrayList<String> info, int invStart) {
        String type = (String) cbType.getSelectedItem();
        setCatalogColumnsForType(type);
        model.setRowCount(0);
        cardsGrid.removeAll();
        if (type.equals("All")) {
	    	for (int i = 0; i < (info.size() - invStart) / 8; i += 1) {
	    		model.addRow(new Object[] {
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
	    	for (int i = 0; i < (info.size() - invStart) / 7; i += 1) {
	    		model.addRow(new Object[] {
	    				info.get(invStart + i * 7 + 0),
	    				info.get(invStart + i * 7 + 1),
	    				info.get(invStart + i * 7 + 2),
	    				info.get(invStart + i * 7 + 3),
	    				info.get(invStart + i * 7 + 4),
	    				info.get(invStart + i * 7 + 5),
	    				info.get(invStart + i * 7 + 6),
	    		});
	    	}
        }
    	/*
        String type = (String) cbType.getSelectedItem();
        String q = txtSearch.getText().trim().toLowerCase();
        setCatalogColumnsForType(type);
        model.setRowCount(0);
        cardsGrid.removeAll();

        if ("Books".equals(type)) {
            List<Book> list = q.isEmpty() ? LibraryData.BOOKS : LibraryData.searchBooks(q);
            for (int i=0;i<list.size();i++) {
            	
                Book b = list.get(i);
                if (i<3) 
                	cardsGrid.add(buildCard(b));
                model.addRow(new Object[]{
                    b.getId(), 
                    b.getTitle(), 
                    b.getAuthor(), 
                    b.getIsbn(), 
                    b.getPublisher(),
                    b.getTotalQuantity(), 
                    LibraryData.availableCountForBook(b.getId())
                });
            }
        } else if ("DVDs".equals(type)) {
            List<Dvd> list = q.isEmpty() ? LibraryData.DVDS : LibraryData.searchDvds(q);
            for (int i=0;i<list.size();i++) {
            	
                Dvd d = list.get(i);
                if (i<3) 
                	cardsGrid.add(buildCard(d));
                model.addRow(new Object[]{
                    d.getId(), 
                    d.getTitle(), 
                    d.getRating(), 
                    d.getRuntimeMinutes()+" min",
                    d.getStudio()==null? "" : d.getStudio(),
                    d.getTotalQuantity(), 
                    LibraryData.availableCountForDvd(d.getId())
                });
            }
        } else if ("Board Games".equals(type)) {
            List<BoardGame> list = q.isEmpty() ? LibraryData.BOARD_GAMES : LibraryData.searchBoardGames(q);
            for (int i=0;i<list.size();i++) {
            	
                BoardGame g = list.get(i);
                if (i<3) 
                	cardsGrid.add(buildCard(g));
                model.addRow(new Object[]{
                    g.getId(), 
                    g.getTitle(),
                    g.getRating(),
                    g.getPlayerCount(), 
                    g.getGameLengthMinutes()+" min",
                    g.getTotalQuantity(), 
                    LibraryData.availableCountForBoardGame(g.getId())
                });
            }
        } else { // All
        	/*
            for (int i=0;i<LibraryData.BOOKS.size();i++) {
                Book b = LibraryData.BOOKS.get(i);
                if (q.isEmpty() || b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q) || b.getIsbn().toLowerCase().contains(q)) {
                    if (cardsGrid.getComponentCount()<3) 
                    	cardsGrid.add(buildCard(b));
                    model.addRow(new Object[]{
                        "Book", b.getId(), 
                        b.getTitle(), 
                        b.getAuthor(), 
                        b.getIsbn(), 
                        b.getPublisher(),
                        b.getTotalQuantity(), 
                        LibraryData.availableCountForBook(b.getId())
                    });
                }
            }
            for (Dvd d : LibraryData.DVDS) {
                if (q.isEmpty() || d.getTitle().toLowerCase().contains(q) || d.getRating().toLowerCase().contains(q)) {
                    if (cardsGrid.getComponentCount()<3) 
                    	cardsGrid.add(buildCard(d));
                    model.addRow(new Object[]{
                        "DVD", d.getId(), 
                        d.getTitle(), 
                        d.getRating(),
                        d.getRuntimeMinutes()+" min", 
                        d.getStudio()==null? "" : 
                        	d.getStudio(),
                        d.getTotalQuantity(), 
                        LibraryData.availableCountForDvd(d.getId())
                    });
                }
            }
            for (BoardGame g : LibraryData.BOARD_GAMES) {
                if (q.isEmpty() || g.getTitle().toLowerCase().contains(q) || g.getRating().toLowerCase().contains(q)) {
                    if (cardsGrid.getComponentCount()<3) 
                    	cardsGrid.add(buildCard(g));
                    model.addRow(new Object[]{
                        "Board Game", 
                        g.getId(), 
                        g.getTitle(), 
                        g.getRating(), 
                        g.getPlayerCount(),
                        g.getGameLengthMinutes()+" min",
                        g.getTotalQuantity(), 
                        LibraryData.availableCountForBoardGame(g.getId())
                    });
                }
            }
        }
        */

        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private JPanel buildCard(Book b) {
        JPanel card = baseCard();
        JLabel title = new JLabel("<html><b>" + b.getTitle() + "</b></html>");
        JLabel d1 = new JLabel("Author: " + b.getAuthor());
        JLabel d2 = new JLabel("ISBN: " + b.getIsbn());
        JLabel d3 = new JLabel("Publisher: " + b.getPublisher());
        JButton details = Theme.darkButton("View");
        details.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Book: " + b.getTitle() + "\nAuthor: " + b.getAuthor() + "\nISBN: " + b.getIsbn() + "\nPublisher: " + b.getPublisher()));
        card.add(title); 
        card.add(d1); 
        card.add(d2); 
        card.add(d3);
        card.add(details);
        return card;
    }
    private JPanel buildCard(Dvd d) {
        JPanel card = baseCard();
        JLabel title = new JLabel("<html><b>" + d.getTitle() + "</b></html>");
        JLabel d1 = new JLabel("Rating: " + d.getRating());
        JLabel d2 = new JLabel("Runtime: " + d.getRuntimeMinutes() + " min");
        JLabel d3 = new JLabel("Studio: " + (d.getStudio()==null? "-" : d.getStudio()));
        JButton details = Theme.darkButton("View");
        details.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "DVD: " + d.getTitle() + "\nRating: " + d.getRating() + "\nRuntime: " + d.getRuntimeMinutes() + " min"));
        card.add(title); 
        card.add(d1); 
        card.add(d2); 
        card.add(d3); 
        card.add(details);
        return card;
    }
    private JPanel buildCard(BoardGame g) {
        JPanel card = baseCard();
        JLabel title = new JLabel("<html><b>" + g.getTitle() + "</b></html>");
        JLabel d1 = new JLabel("Rating: " + g.getRating());
        JLabel d2 = new JLabel("Players: " + g.getPlayerCount());
        JLabel d3 = new JLabel("Length: " + g.getGameLengthMinutes() + " min");
        JButton details = Theme.darkButton("View");
        details.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Board Game: " + g.getTitle() + "\nPlayers: " + g.getPlayerCount() + "\nLength: " + g.getGameLengthMinutes() + " min"));
        card.add(title); 
        card.add(d1); 
        card.add(d2); 
        card.add(d3);
        card.add(details);
        return card;
    }

    private JPanel baseCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230)),
                new EmptyBorder(10,10,10,10)));
        return p;
    }
}
