package server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

import account.*;
import message.*;
import inventory.*;
import library.*;


public class LibraryServer {
	public static void main(String[] args) {
		ServerSocket server = null;
		
		LibrarySystem librarySystem = new LibrarySystem("Double Library System");
		librarySystem.addLibrary(new Library(0, "The First One", new Inventory(new ArrayList<Media>())));
		librarySystem.addLibrary(new Library(1, "The Other One", new Inventory(new ArrayList<Media>())));
		// TODO: read inventory from file into each library's Inventory object
		Inventory firstOneInventory = librarySystem.getlibrary(0).getInventory();
		firstOneInventory.addMedia(new Book("The Book", "The Publishing House", "Horror", 5, 5, "Mr. Idk", 340.5, "351-64534-343"));
		firstOneInventory.addMedia(new Book("Book, Too!", "The Publishing House", "Comedy", 3, 3, "Mr. Idk", 121.5, "987-245345"));
		firstOneInventory.addMedia(new Book("You Never Know", "Different Publisher", "Mystery", 2, 2, "Mrs. _", 145.6, "35-234343"));
		firstOneInventory.addMedia(new DVD("The Movie", "Paramount", "Drama", 5, 5, Rating.PG_13, 120));
		firstOneInventory.addMedia(new DVD("Movie but Worse", "Paramount", "Drama", 3, 3, Rating.PG, 180));
		firstOneInventory.addMedia(new DVD("You Never Know", "Innovation Prod.", "Comedy", 2, 2, Rating.G, 140));
		firstOneInventory.addMedia(new BoardGame("Something's Missing", "Games Unlimited", "Party", 2, 2, Rating.G, 2, 4, 120));
		firstOneInventory.addMedia(new BoardGame("Dreamers' Gate", "Games Unlimited", "Tabletop RPG", 3, 3, Rating.R, 1, 4, 600));
		Inventory secondOneInventory = librarySystem.getlibrary(1).getInventory();
		secondOneInventory.addMedia(new Book("Help", "Publishers", "Horror", 4, 4, "Milton", 325.7, "351-12334-343"));
		secondOneInventory.addMedia(new Book("Why", "Incorporated", "Drama", 2, 2, "Hilton", 411.7, "45434-3423434"));
		secondOneInventory.addMedia(new DVD("Help", "A24", "Horror", 3, 3, Rating.R, 200));
		secondOneInventory.addMedia(new BoardGame("Trains", "Games Unlimited", "Dice Game", 2, 2, Rating.NC_17, 3, 6, 240));
		secondOneInventory.addMedia(new BoardGame("Gains", "Games Unlimited", "Party Game", 3, 3, Rating.PG, 4, 6, 30));

		// TODO: read inventory from file into each library's Inventory object
		AccountsDirectory librarySystemAccounts = librarySystem.getAccountsDirectory();
		librarySystemAccounts.registerNewAccount(Permission.MEMBER, "member@test.test", "test123", "Tester Testington", new Date(2024 - 1900, 0, 1));
		librarySystemAccounts.registerNewAccount(Permission.ADMIN, "admin@test.test", "admin123", "Iam Admin", new Date(2000 - 1900, 5, 20));
		
		try {
			server = new ServerSocket(56789);
			server.setReuseAddress(true);
			
			InetAddress serverAddress = InetAddress.getLocalHost();
			System.out.println("server IPv4 address: " + serverAddress.getHostAddress());
			
			while (true) {
				Socket client = server.accept();
				System.out.println("new client IPv4 address: " + client.getInetAddress().getHostAddress());
//				ClientHandler clientHandler = new ClientHandler(client);
				ClientHandler clientHandler = new ClientHandler(client, librarySystem);
				(new Thread(clientHandler)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private LibrarySystem librarySystem;
		
//		public ClientHandler(Socket clientSocket) {
		public ClientHandler(Socket clientSocket, LibrarySystem librarySystem) {
			this.clientSocket = clientSocket;
			this.librarySystem = librarySystem;
		}
		
		public void run() {
			ObjectOutputStream writerToClient = null;
			ObjectInputStream readerFromClient = null;
			try {
				writerToClient = new ObjectOutputStream(clientSocket.getOutputStream());
				readerFromClient = new ObjectInputStream(clientSocket.getInputStream());
				Message messageFromClient = null, messageToClient = null;
				
				/*
				// BACKEND DATA (should ideally get it to load in from files later)
				AccountsDirectory accountsDirectory = new AccountsDirectory();
				accountsDirectory.registerNewAccount(Permission.MEMBER, "member@test.test", "test123", "Tester Testington", new Date(2024 - 1900, 0, 1));
				accountsDirectory.registerNewAccount(Permission.ADMIN, "admin@test.test", "admin123", "Iam Admin", new Date(2000 - 1900, 5, 20));
				Inventory inventory = new Inventory(new ArrayList<Media>());
				inventory.addMedia(new Book("The Book", "The Publishing House", "Horror", 5, 5, "Mr. Idk", 340.5, "351-64534-343"));
				inventory.addMedia(new Book("Book, Too!", "The Publishing House", "Comedy", 3, 3, "Mr. Idk", 121.5, "987-245345"));
				inventory.addMedia(new Book("You Never Know", "Different Publisher", "Mystery", 2, 2, "Mrs. _", 145.6, "35-234343"));
				inventory.addMedia(new DVD("The Movie", "Paramount", "Drama", 5, 5, Rating.PG_13, 120));
				inventory.addMedia(new DVD("Movie but Worse", "Paramount", "Drama", 3, 3, Rating.PG, 180));
				inventory.addMedia(new DVD("You Never Know", "Innovation Prod.", "Comedy", 2, 2, Rating.G, 140));
				inventory.addMedia(new BoardGame("Something's Missing", "Games Unlimited", "Party", 2, 2, Rating.G, 2, 4, 120));
				inventory.addMedia(new BoardGame("Dreamers' Gate", "Games Unlimited", "Tabletop RPG", 3, 3, Rating.R, 1, 4, 600));
				*/
				
				AccountsDirectory accountsDirectory = librarySystem.getAccountsDirectory();
				Library library = null;
				Inventory inventory = null;
				LoanRepository loanRepository = new LoanRepository();
				HoldsRepository holdRepository = new HoldsRepository();
				Account account = null;
				try {
					while (true) {
						messageFromClient = (Message) readerFromClient.readObject();
						// I BELIEVE THAT THIS IS WHERE RequestHandler FACADE WILL BE CALLED
						// JUST KEEPING LOGIC RIGHT HERE TEMPORARILY FOR TESTING PURPOSES
						System.out.println(messageFromClient);
						// System.out.println("Is logged in? " + isLoggedIn);
						ArrayList<String> info = new ArrayList<String>();
						if (messageFromClient.getAction() == Action.GET_DASHBOARD) {
							if (messageFromClient.getInfo().size() > 0) {
								for (Library l : librarySystem.getLibraries()) {
									if (l.getName().equalsIgnoreCase(messageFromClient.getInfo().getFirst())) {
										library = l;
										inventory = library.getInventory();
									}
								}
							}
							messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_DASHBOARD, Status.SUCCESS, info);
							info.add(library.getName());
//							info.add("Our Little Library");
//							int items = 2;
//							info.add(Integer.toString(items));
//							// TODO:
//							// inventory = Library.getInventory();
//							// for item in inventory:
//							// add each piece of info
							for (Media media : inventory.getMediaItems()) {
								if (media instanceof Book) {
									addBookToInfo(info, (Book) media, true);
								} else if (media instanceof DVD) {
									addDVDToInfo(info, (DVD) media, true);
								} else if (media instanceof BoardGame) {
									addBoardGameToInfo(info, (BoardGame) media, true);
								} else {
									System.out.println("Media of unexpected/unknown type found in inventory");
								}
							}
							
							writerToClient.writeObject(messageToClient);
						} else if (messageFromClient.getAction() == Action.GET_SEARCH) {
							messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_SEARCH, Status.SUCCESS, info);
							String requestMediaType = messageFromClient.getInfo().getFirst();
							boolean shouldIncludeType = requestMediaType.equals("All");
							String requestMediaTitle = messageFromClient.getInfo().getLast();
							ArrayList<Media> relevantInventory = requestMediaTitle.equals("") ? inventory.getMediaItems() : inventory.searchByTitle(requestMediaTitle);
							for (Media media : relevantInventory) {
								if ((requestMediaType.equals("All") || requestMediaType.equals("Books")) && media instanceof Book) {
									addBookToInfo(info, (Book) media, shouldIncludeType);
								} else if ((requestMediaType.equals("All") || requestMediaType.equals("DVDs")) && media instanceof DVD) {
									addDVDToInfo(info, (DVD) media, shouldIncludeType);
								} else if ((requestMediaType.equals("All") || requestMediaType.equals("Board Games")) && media instanceof BoardGame) {
									addBoardGameToInfo(info, (BoardGame) media, shouldIncludeType);
								} else {
									System.out.println("Request type unknown or media type in Inventory unknown");
								}
							}
							
							writerToClient.writeObject(messageToClient);
						} else if (account == null) {
							// info.add(Integer.toString(inventory.getNumMedia()));
							if (messageFromClient.getAction() == Action.LOGIN) {
								account = accountsDirectory.login(messageFromClient.getInfo().getFirst(), messageFromClient.getInfo().getLast());
								if (account == null) {
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.FAILURE, null);
									writerToClient.writeObject(messageToClient);
									continue;
								} else {
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.loadLoansFromFile(filename);
									
									// info = new ArrayList<>();
									
									info.add(account instanceof Admin ? "ADMIN" : "MEMBER");
									info.add(account.getFullName());
//									addFullInventoryDummyData(info);
									
									if (info.getFirst().equals("MEMBER")) {
										info.add(Integer.toString(inventory.getNumMedia()));
										// inventory info
//										info.add(Integer.toString(inventory.getMediaItems().size())); // number of inventory items returned
//										addFullInventoryDummyData(info);
										
										// loan info
//										info.add("3");
//										info.add("BOOK");
//										info.add("3");
//										info.add("TITLE");
//										info.add("2025-11-15");
//										info.add("2025-12-01");
//										info.add("4");
//										info.add("");
										
										for (Media media : inventory.getMediaItems()) {
											if (media instanceof Book) {
												addBookToInfo(info, (Book) media, true);
											} else if (media instanceof DVD) {
												addDVDToInfo(info, (DVD) media, true);
											} else if (media instanceof BoardGame) {
												addBoardGameToInfo(info, (BoardGame) media, true);
											} else {
												System.out.println("Media of unexpected/unknown type found in inventory");
											}
										}
									} else if (info.getFirst().equals("ADMIN")) {
										addInventoryToInfoAdmin(inventory, info);
//								    	info.add("BOOK");
//								    	info.add("1");
//								    	info.add("1234567890");
//								    	info.add("This Title");
//								    	info.add("Author");
//								    	info.add("Publishing House");
//								    	info.add("Horror");
//								    	info.add("4");
//								    	info.add("2");
//								    	
//								    	info.add("DVD");
//								    	info.add("2");
//								    	info.add("This Movie");
//								    	info.add("R");
//								    	info.add("150");
//								    	info.add("4");
//								    	info.add("2");
//								    	
//								    	info.add("BOARD_GAME");
//								    	info.add("3");
//								    	info.add("Fun Game");
//								    	info.add("12+");
//								    	info.add("2-4");
//								    	info.add("~120");
//								    	info.add("5");
//								    	info.add("5");
									}
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.SUCCESS, info);
									writerToClient.writeObject(messageToClient);
									continue;
								}
							} else if (messageFromClient.getAction() == Action.REGISTER) {
								boolean newAccountIsAdmin = Boolean.parseBoolean(messageFromClient.getInfo().getLast());
								Account newAccount = accountsDirectory.registerNewAccount(newAccountIsAdmin ? Permission.ADMIN : Permission.MEMBER,
										messageFromClient.getInfo().getFirst(), messageFromClient.getInfo().get(1),
										messageFromClient.getInfo().get(2), new Date(messageFromClient.getInfo().get(3)));
								if (newAccount != null) {
									System.out.println(newAccount);
									System.out.println(newAccount.getBirthday());
									info.add(newAccountIsAdmin ? "Admin" : "Member");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.REGISTER, Status.SUCCESS, info);
								} else {
									info.add("Account associated with this email address already exists");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.REGISTER, Status.FAILURE, info);
								}
								
							} else {
								info.add("This action is not valid until login!");
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.FAILURE, info);
							}
							writerToClient.writeObject(messageToClient);
						} else {
							if (messageFromClient.getAction() == Action.GET_CHECKOUTS) {
//								Commenting out for now in case code doesn't work and need to go back to something that does work
//								info.add("Harry Potter");
//								info.add("Mouse Paint");
//								info.add("If You Give a Mouse a Cookie");
//								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_CHECKOUTS, Status.SUCCESS, info);
//								writerToClient.writeObject(messageToClient);
								
								ArrayList<Loan> all = loanRepository.getHistory();
								int memberId = account.getId();
								
								ArrayList<Loan> activeLoans = new ArrayList<>();
								for (Loan l : all) {
									if (l.getMemberId() == memberId && l.getReturnedDate() == null) {
										activeLoans.add(l);
									}
								}
								
								info.clear();
								info.add(Integer.toString(activeLoans.size()));
								
								for (Loan l : activeLoans) {
									Media media = findMediaInInventory(inventory, l.getMediaId());
									if (media == null) {
										continue;
									}
									
									info.add(Integer.toString(l.getLoanId()));
									
									if (media instanceof Book) {
										info.add("BOOK");
										info.add(Integer.toString(media.getId()));
										Book b = (Book)media;
										info.add(b.getTitle());
									} else if (media instanceof DVD) {
										info.add("DVD");
										info.add(Integer.toString(media.getId()));
										DVD d = (DVD)media;
										info.add(d.getTitle());
									} else if (media instanceof BoardGame) {
										info.add("BOARD GAME");
										info.add(Integer.toString(media.getId()));
										BoardGame bg = (BoardGame)media;
										info.add(bg.getTitle());
									}
									
									info.add(l.getCheckoutDate().toString());
									info.add(l.getDueDate().toString());
									info.add("");
									info.add("");
								}
								
//								Commenting this out because this works with dummy data
//								for (Loan l : activeLoans) {
//									//When Inventory is integrated, necessary code goes in here
//									String title = lookupDummyTitle(l.getMediaId());
//									info.add(Integer.toString(l.getLoanId()));
//									info.add("BOOK");
//									info.add(Integer.toString(l.getMediaId()));
//									info.add(title);
//									info.add(l.getCheckoutDate().toString());
//									info.add(l.getDueDate().toString());
//									info.add("");		//grace period??
//									info.add(l.getReturnedDate() == null ? "" : l.getReturnedDate().toString());
//								}
								
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_CHECKOUTS, Status.SUCCESS, info);							
								writerToClient.writeObject(messageToClient);
								
							} else if (messageFromClient.getAction() == Action.CHECKOUT) {
//								Commenting out for now in case code doesn't work and need to go back to something that does work								
//								info.add("Networking 101 successfully checked out!");
//								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.SUCCESS, info);
//								writerToClient.writeObject(messageToClient);
								
								ArrayList<String> str = new ArrayList<>();
								
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								long dueMillis = Long.parseLong(messageFromClient.getInfo().get(1));
								Date due = new Date(dueMillis);
								
								Loan newLoan = loanRepository.checkoutMedia(mediaId, account.getId(), due, (Member)account, inventory, holdRepository);
//								Loan newLoan = loanRepository.checkoutMedia(account.getId(), mediaId, dueMillis);
								
								if (newLoan == null) {
									str.add("Checkout failed!");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.FAILURE, str);
								} else {
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.saveLoansToFile(filename);
									
									str.add("Checkout successful!");	
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.SUCCESS, str);
								}
								
								writerToClient.writeObject(messageToClient);
							} else if(messageFromClient.getAction() == Action.RETURN) {
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								
								boolean success = loanRepository.returnMedia(mediaId, account.getId(), inventory);
								if (!success) {
									info.add("Return failed.");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.RETURN, Status.FAILURE, info);
								} else {
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.saveLoansToFile(filename);
									
									info.add("Return successfull!");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.RETURN, Status.SUCCESS, info);
								}
								
								writerToClient.writeObject(messageToClient); 
								continue;
							} else if (messageFromClient.getAction() == Action.PLACE_HOLD) {
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								long untilMillis = Long.parseLong(messageFromClient.getInfo().get(1));
								
								Hold h = holdRepository.placeHold(mediaId, account.getId(), new Date(untilMillis), (Member)account);
								
								if (h == null) {
									info.add("Hold failed.");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.PLACE_HOLD, Status.FAILURE, info);
								} else {
									String filename = "holds_" + account.getId() + ".txt";
									holdRepository.saveHoldToFile(filename);
									
									info.add("Hold placed!");
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.PLACE_HOLD, Status.SUCCESS, info);
								}
								
								writerToClient.writeObject(messageToClient); 
								continue;
							} else if (messageFromClient.getAction() == Action.GET_HOLDS) {
								
								
							} else if (messageFromClient.getAction() == Action.CANCEL_HOLD) {
								
								
							} else if (messageFromClient.getAction() == Action.GET_PROFILE) {
							
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_PROFILE, Status.SUCCESS, info);
								info.add(account.getFullName());
								info.add(account.getBirthday().toString());
								info.add(account.getEmail());
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.SET_PROFILE) {
								account.setFullName(messageFromClient.getInfo().getFirst());
								account.setBirthday(new Date(messageFromClient.getInfo().get(1)));
								account.setEmail(messageFromClient.getInfo().get(2));
								System.out.println(account);
								System.out.println(account.getBirthday());
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.SET_PROFILE, Status.SUCCESS, null);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.ADD_BOOK) {
								ArrayList<String> newBookAttr = messageFromClient.getInfo();
								int newQuant = Integer.parseInt(newBookAttr.get(5));
								double newDd = Double.parseDouble(newBookAttr.getLast());
								inventory.addMedia(new Book(newBookAttr.get(1), newBookAttr.get(3), newBookAttr.get(4), newQuant, newQuant, newBookAttr.get(2), newDd, newBookAttr.get(0)));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.ADD_BOOK, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.EDIT_BOOK) {
								ArrayList<String> newBookAttr = messageFromClient.getInfo();
								Book editBook = (Book) inventory.searchByID(Integer.parseInt(newBookAttr.getFirst())).getFirst();
								editBook.setIsbn(newBookAttr.get(1));
								editBook.setTitle(newBookAttr.get(2));
								editBook.setAuthor(newBookAttr.get(3));
								editBook.setPublisher(newBookAttr.get(4));
								editBook.setGenre(newBookAttr.get(5));
								editBook.setTotalQuantity(Integer.parseInt(newBookAttr.get(6)));
								editBook.setQuantityAvailable(Integer.parseInt(newBookAttr.get(7)));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.EDIT_BOOK, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.DELETE_BOOK || messageFromClient.getAction() == Action.DELETE_DVD || messageFromClient.getAction() == Action.DELETE_GAME) {
								int id = Integer.parseInt(messageFromClient.getInfo().getFirst());
								boolean found = false;
								for (int i = 0; i < inventory.getMediaItems().size(); i += 1) {
									if (inventory.getMediaItems().get(i).getId() == id) {
										found = true;
										inventory.getMediaItems().remove(i);
										messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), messageFromClient.getAction(), Status.SUCCESS, info);
									}
								}
								if (!found) {
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), messageFromClient.getAction(), Status.FAILURE, info);
								}
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.ADD_DVD) {
								ArrayList<String> newDvdAttr = messageFromClient.getInfo();
								int newQuant = Integer.parseInt(newDvdAttr.get(3));
								Rating newRating = stringToRating(newDvdAttr.get(4));
								int newRuntime = Integer.parseInt(newDvdAttr.get(5));
								inventory.addMedia(new DVD(newDvdAttr.get(0), newDvdAttr.get(1), newDvdAttr.get(2), newQuant, newQuant, newRating, newRuntime));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.ADD_DVD, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.EDIT_DVD) {
								ArrayList<String> newDvdAttr = messageFromClient.getInfo();
								DVD editDvd = (DVD) inventory.searchByID(Integer.parseInt(newDvdAttr.getFirst())).getFirst();
								editDvd.setTitle(newDvdAttr.get(1));
								editDvd.setAgeRating(stringToRating(newDvdAttr.get(2)));
								editDvd.setRunTime(Integer.parseInt(newDvdAttr.get(3)));
								editDvd.setTotalQuantity(Integer.parseInt(newDvdAttr.get(4)));
								editDvd.setQuantityAvailable(Integer.parseInt(newDvdAttr.get(5)));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.EDIT_DVD, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.ADD_GAME) {
								ArrayList<String> newGameAttr = messageFromClient.getInfo();
								int newQuant = Integer.parseInt(newGameAttr.get(7));
								Rating newRating = stringToRating(newGameAttr.get(3));
								int newMinPlayers = Integer.parseInt(newGameAttr.get(4));
								int newMaxPlayers = Integer.parseInt(newGameAttr.get(5));
								int newLength = Integer.parseInt(newGameAttr.get(6));
								inventory.addMedia(new BoardGame(newGameAttr.get(0), newGameAttr.get(2), newGameAttr.get(1), newQuant, newQuant, newRating, newMinPlayers, newMaxPlayers, newLength));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.ADD_GAME, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.EDIT_GAME) {
								ArrayList<String> newGameAttr = messageFromClient.getInfo();
								BoardGame editGame = (BoardGame) inventory.searchByID(Integer.parseInt(newGameAttr.getFirst())).getFirst();
								editGame.setTitle(newGameAttr.get(1));
								editGame.setRating(stringToRating(newGameAttr.get(2)));
								editGame.setPlayerCountMin(Integer.parseInt(newGameAttr.get(3)));
								editGame.setPlayerCountMax(Integer.parseInt(newGameAttr.get(4)));
								editGame.setGameLength(Integer.parseInt(newGameAttr.get(5)));
								editGame.setTotalQuantity(Integer.parseInt(newGameAttr.get(6)));
								editGame.setQuantityAvailable(Integer.parseInt(newGameAttr.get(7)));
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.EDIT_GAME, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.LOGOUT) {
								account = null;
								info.add("Our Little Library");
								for (Media media : inventory.getMediaItems()) {
									if (media instanceof Book) {
										addBookToInfo(info, (Book) media, true);
									} else if (media instanceof DVD) {
										addDVDToInfo(info, (DVD) media, true);
									} else if (media instanceof BoardGame) {
										addBoardGameToInfo(info, (BoardGame) media, true);
									} else {
										System.out.println("Media of unexpected/unknown type found in inventory");
									}
								}
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGOUT, Status.SUCCESS, info);
								writerToClient.writeObject(messageToClient);
								
								// writerToClient.close();
								// readerFromClient.close();
								// clientSocket.close();
								// return;
							}
						}
						
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (writerToClient != null) {
						writerToClient.close();
					}
					if (readerFromClient != null) {
						readerFromClient.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static void addFullInventoryDummyData(ArrayList<String> info) {
		info.add("DVD");
		info.add("1");
		info.add("EEAAO");
		info.add("R");
		info.add("140 min");
		info.add("Daniels");
		info.add("5");
		info.add("3");
		
		info.add("Book");
		info.add("4");
		info.add("Booksmart");
		info.add("B. Smart");
		info.add("1234566789");
		info.add("Idk");
		info.add("4");
		info.add("0");
	}
	
	public static String lookupDummyTitle(int mediaId) {
		switch(mediaId) {
			case 1: return "EEAA0";
			case 2: return "The Housemaid";
			case 3: return "Charlotte's Web";
			case 4: return "Booksmart";
			default: return "Unknown Title";
		}
	}
	
	private static Media findMediaInInventory(Inventory inventory, int mediaId) {
		for (Media m : inventory.getMediaItems()) {
			if (m.getId() == mediaId) {
				return m;
			}
		}
		return null;
	}
	
	private static void addBookToInfo(ArrayList<String> info, Book book, boolean shouldIncludeType) {
		if (shouldIncludeType) {
			info.add("Book");
		}
		info.add("" + book.getId());
		info.add(book.getTitle());
		info.add(book.getAuthor());
		info.add(book.getIsbn());
		info.add(book.getPublisher());
		info.add("" + book.getTotalQuantity());
		info.add("" + book.getQuantityAvailable());
	}
	private static void addDVDToInfo(ArrayList<String> info, DVD dvd, boolean shouldIncludeType) {
		if (shouldIncludeType) {
			info.add("DVD");
		}
		info.add("" + dvd.getId());
		info.add(dvd.getTitle());
		info.add(dvd.getAgeRating());
		info.add(dvd.getRunTime() + " min");
		info.add(dvd.getPublisher());
		info.add("" + dvd.getTotalQuantity());
		info.add("" + dvd.getQuantityAvailable());
	}
	private static void addBoardGameToInfo(ArrayList<String> info, BoardGame boardGame, boolean shouldIncludeType) {
		if (shouldIncludeType) {
			info.add("Board Game");
		}
		info.add("" + boardGame.getId());
		info.add(boardGame.getTitle());
		info.add(boardGame.getRating().toString());
		info.add(boardGame.getPlayerCountMin() + "-" + boardGame.getPlayerCountMax());
		info.add(boardGame.getGameLength() + " min");
		info.add("" + boardGame.getTotalQuantity());
		info.add("" + boardGame.getQuantityAvailable());
	}
	private static void addInventoryToInfoAdmin(Inventory inventory, ArrayList<String> info) {
		for (Media media : inventory.getMediaItems()) {
			if (media instanceof Book) {
				Book book = (Book) media;
				info.add("BOOK");
				info.add("" + book.getId());
				info.add(book.getIsbn());
				info.add(book.getTitle());
				info.add(book.getAuthor());
				info.add(book.getPublisher());
				info.add(book.getGenre());
				info.add("" + book.getTotalQuantity());
				info.add("" + book.getQuantityAvailable());
			} else if (media instanceof DVD) {
				DVD dvd = (DVD) media;
				info.add("DVD");
				info.add("" + dvd.getId());
				info.add(dvd.getTitle());
				info.add(dvd.getAgeRating());
				info.add("" + dvd.getRunTime());
				info.add("" + dvd.getTotalQuantity());
				info.add("" + dvd.getQuantityAvailable());
			} else if (media instanceof BoardGame) {
				BoardGame boardGame = (BoardGame) media;
				info.add("BOARD_GAME");
				info.add("" + boardGame.getId());
				info.add(boardGame.getTitle());
				info.add(boardGame.getRating().toString());
				info.add(boardGame.getPlayerCountMin() + "-" + boardGame.getPlayerCountMax());
				info.add("" + boardGame.getGameLength());
				info.add("" + boardGame.getTotalQuantity());
				info.add("" + boardGame.getQuantityAvailable());
			} else {
				System.out.println("Unrecognized media type in Inventory");
			}
		}
	}
	private static Rating stringToRating(String rating) {
		if (rating.equalsIgnoreCase("G")) {
			return Rating.G;
		} else if (rating.equalsIgnoreCase("PG")) {
			return Rating.PG;
		} else if (rating.equalsIgnoreCase("PG-13")) {
			return Rating.PG_13;
		} else if (rating.equalsIgnoreCase("NC-17")) {
			return Rating.NC_17;
		} else if (rating.equalsIgnoreCase("R")) {
			return Rating.R;
		} else {
			return Rating.UNRATED;
		}
	}
}
