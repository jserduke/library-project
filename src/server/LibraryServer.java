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
		
    	File file = new File("new-folder" + File.separator + "logs2.txt"); //open logs.txt
    	file.getParentFile().mkdir();
    	try {
    		if(file.createNewFile()) { // If a NEW file is created, add text to it
				FileWriter myWriter = new FileWriter("new-folder/logs2.txt"); //Create a writer
				myWriter.write("Logging testing."); //Write this string
				myWriter.close(); //close the writer
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		LibrarySystem librarySystem = new LibrarySystem("Double Library System");
		librarySystem.addLibrary(new Library(0, "Our Little Library", new Inventory(new ArrayList<Media>())));
		librarySystem.addLibrary(new Library(1, "OBL", new Inventory(new ArrayList<Media>())));
		// TODO: read inventory from file into each library's Inventory object
		Inventory ollInventory = librarySystem.getlibrary(0).getInventory();
		ollInventory.loadInventoryFromFile("ollinv");
		// System.out.println(ollInventory);
		// System.out.println(Media.getNextMediaId());
		/*
		ollInventory.addMedia(new Book("The Book", "The Publishing House", "Horror", 5, 5, "Mr. Idk", 340.5, "351-64534-343"));
		ollInventory.addMedia(new Book("Book, Too!", "The Publishing House", "Comedy", 3, 3, "Mr. Idk", 121.5, "987-245345"));
		ollInventory.addMedia(new Book("You Never Know", "Different Publisher", "Mystery", 2, 2, "Mrs. _", 145.6, "35-234343"));
		ollInventory.addMedia(new DVD("The Movie", "Paramount", "Drama", 5, 5, Rating.PG_13, 120));
		ollInventory.addMedia(new DVD("Movie but Worse", "Paramount", "Drama", 3, 3, Rating.PG, 180));
		ollInventory.addMedia(new DVD("You Never Know", "Innovation Prod.", "Comedy", 2, 2, Rating.G, 140));
		ollInventory.addMedia(new BoardGame("Something's Missing", "Games Unlimited", "Party", 2, 2, Rating.G, 2, 4, 120));
		ollInventory.addMedia(new BoardGame("Dreamers' Gate", "Games Unlimited", "Tabletop RPG", 3, 3, Rating.R, 1, 4, 600));
		ollInventory.saveInventoryToFile("ollinv");
		*/
		Inventory oblInventory = librarySystem.getlibrary(1).getInventory();
		oblInventory.loadInventoryFromFile("oblinv");
		// System.out.println(oblInventory);
		// System.out.println(Media.getNextMediaId());
		/*
		oblInventory.addMedia(new Book("Help", "Publishers", "Horror", 4, 4, "Milton", 325.7, "351-12334-343"));
		oblInventory.addMedia(new Book("Why", "Incorporated", "Drama", 2, 2, "Hilton", 411.7, "45434-3423434"));
		oblInventory.addMedia(new DVD("Help", "A24", "Horror", 3, 3, Rating.R, 200));
		oblInventory.addMedia(new BoardGame("Trains", "Games Unlimited", "Dice Game", 2, 2, Rating.NC_17, 3, 6, 240));
		oblInventory.addMedia(new BoardGame("Gains", "Games Unlimited", "Party Game", 3, 3, Rating.PG, 4, 6, 30));
		oblInventory.saveInventoryToFile("oblinv");
		*/
		// System.out.println(Media.getNextMediaId());
		// System.exit(0);

		// TODO: read inventory from file into each library's Inventory object
		AccountsDirectory librarySystemAccounts = librarySystem.getAccountsDirectory();
		librarySystemAccounts.registerNewAccount(Permission.MEMBER, "member@test.test", "test123", "Tester Testington", new Date(2024 - 1900, 0, 1));
		librarySystemAccounts.registerNewAccount(Permission.MEMBER, "member", "member", "Member", new Date(2015 - 1900, 0, 1));
		librarySystemAccounts.registerNewAccount(Permission.ADMIN, "admin@test.test", "admin123", "Iam Admin", new Date(2000 - 1900, 5, 20));
		librarySystemAccounts.registerNewAccount(Permission.ADMIN, "admin", "admin", "Yosoy Admin", new Date(2000 - 1900, 5, 20));
		
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
				
				AccountsDirectory accountsDirectory = librarySystem.getAccountsDirectory();
				Library library = null;
				Inventory inventory = null;
				LoanRepository loanRepository = new LoanRepository();
				HoldsRepository holdRepository = new HoldsRepository();
				Account account = null;
				FileWriter loggingWriter = null;
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
							messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.GET_DASHBOARD, Status.SUCCESS, info);
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
						} else if (messageFromClient.getAction() == Action.GET_SEARCH || messageFromClient.getAction() == Action.GET_SEARCH_MEMBER) {
							messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), messageFromClient.getAction(), Status.SUCCESS, info);
							String requestMediaType = messageFromClient.getInfo().getFirst();
							boolean shouldIncludeType = requestMediaType.equals("All") || messageFromClient.getAction() == Action.GET_SEARCH_MEMBER;
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
						// if user isn't logged in yet
						} else if (account == null) {
							if (messageFromClient.getAction() == Action.LOGIN) {
								// search for account with corresponding username (getFirst) and password (getLast)
								account = accountsDirectory.login(messageFromClient.getInfo().getFirst(), messageFromClient.getInfo().getLast());
								// if no account with username and password was found:
								if (account == null) {
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.FAILURE, null);
									writerToClient.writeObject(messageToClient);
									continue;
								// if account matching credentials was found:
								} else {
							    	File loggingFile = new File("logs" + File.separator + "logs_" + account.getId() + "_" + (new Date()).getTime() + ".txt");
							    	loggingFile.getParentFile().mkdir();
							    	loggingFile.createNewFile();
									loggingWriter = new FileWriter(loggingFile, true);
									loggingWriter.write("LOGGING FOR " + account.getFullName() + "'s " + (new Date()) + " SESSION @ " + library.getName() + "\n");
									
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.loadLoansFromFile(filename);
									
									String holdsFile = "holds_" + account.getId() + ".txt";
									holdRepository.saveHoldToFile(holdsFile);
									
									
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
									appendLog(loggingWriter, "Successfully logged into the " + info.getFirst() + " portal");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.SUCCESS, info);
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
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.REGISTER, Status.SUCCESS, info);
								} else {
									info.add("Account associated with this email address already exists");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.REGISTER, Status.FAILURE, info);
								}
								
							} else {
								info.add("This action is not valid until login!");
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.FAILURE, info);
							}
							writerToClient.writeObject(messageToClient);
						} else {
							if (messageFromClient.getAction() == Action.GET_CHECKOUTS) {								
								ArrayList<Loan> all = loanRepository.getHistory();
								int memberId = account.getId();
								
								ArrayList<Loan> activeLoans = new ArrayList<>();
								for (Loan l : all) {
									if (l.getMemberId() == memberId) {
										activeLoans.add(l);
									}
								}
								
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
									
									if (l.getReturnedDate() == null) {
										info.add("");
									} else {
										info.add(l.getReturnedDate().toString());
									}
//									info.add("");
//									info.add("");
								}
								
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.GET_CHECKOUTS, Status.SUCCESS, info);							
								writerToClient.writeObject(messageToClient);
								
							} else if (messageFromClient.getAction() == Action.CHECKOUT) {								
								ArrayList<String> str = new ArrayList<>();
								
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								long dueMillis = Long.parseLong(messageFromClient.getInfo().get(1));
								Date due = new Date(dueMillis);
								
								Loan newLoan = loanRepository.checkoutMedia(mediaId, account.getId(), due, (Member)account, inventory, holdRepository);
								
								if (newLoan == null) {
									appendLog(loggingWriter, "Failed to checkout media with an ID of " + mediaId);
									str.add("Checkout failed!");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.FAILURE, str);
								} else {
									appendLog(loggingWriter, "Successfully checked out " + inventory.searchByID(mediaId).getFirst().getTitle());
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.saveLoansToFile(filename);
									
									str.add("Checkout successful!");	
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.SUCCESS, str);
								}
								
								writerToClient.writeObject(messageToClient);
							} else if(messageFromClient.getAction() == Action.RETURN) {
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								
								boolean success = loanRepository.returnMedia(mediaId, account.getId(), inventory);
								if (!success) {
									appendLog(loggingWriter, "Failed to return media with an ID of " + mediaId);
									info.add("Return failed.");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.RETURN, Status.FAILURE, info);
								} else {
									appendLog(loggingWriter, "Successfully returned " + inventory.searchByID(mediaId).getFirst().getTitle());
									String filename = "loans_" + account.getId() + ".txt";
									loanRepository.saveLoansToFile(filename);
									
									info.add("Return successfull!");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.RETURN, Status.SUCCESS, info);
								}
								
								writerToClient.writeObject(messageToClient); 
								continue;
							} else if (messageFromClient.getAction() == Action.PLACE_HOLD) {
								int mediaId = Integer.parseInt(messageFromClient.getInfo().get(0));
								long untilMillis = Long.parseLong(messageFromClient.getInfo().get(1));
								
								Hold h = holdRepository.placeHold(mediaId, account.getId(), new Date(untilMillis), (Member)account, inventory);
								
								if (h == null) {
									appendLog(loggingWriter, "Failed to place a hold for media with an ID of " + mediaId);
									info.add("Hold failed.");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.PLACE_HOLD, Status.FAILURE, info);
								} else {
									appendLog(loggingWriter, "Sucessfully placed a hold for " + inventory.searchByID(mediaId).getFirst().getTitle());
									String filename = "holds_" + account.getId() + ".txt";
									holdRepository.saveHoldToFile(filename);
									
									info.add("Hold placed!");
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.PLACE_HOLD, Status.SUCCESS, info);
								}
								
								writerToClient.writeObject(messageToClient); 
								continue;
							} else if (messageFromClient.getAction() == Action.GET_HOLDS) {
								ArrayList<String> info1 = new ArrayList<>();
								
								//Remove expired holds
								int removed = holdRepository.cancelExpiredHolds();
								if (removed > 0) {
									String filename = "holds_" + account.getId() + ".txt";
									holdRepository.saveHoldToFile(filename);
								}
								
								ArrayList<Hold> activeHolds = new ArrayList<>();
								
								if (account instanceof Admin) {
									activeHolds.addAll(holdRepository.getHolds());
								} else {
									for (Hold h : holdRepository.getHolds()) {
										if (h.getMemberId() == account.getId()) {
											activeHolds.add(h);
										}
									}
								}
								
								info1.add(Integer.toString(activeHolds.size()));
								
								for (Hold h : activeHolds) {
									info1.add(Integer.toString(h.getHoldId()));
									info1.add(Integer.toString(h.getMediaId()));
									
									String title = "(unknown)";
									Media media = findMediaInInventory(inventory, h.getMediaId());
//									ArrayList<Media> mediaList = inventory.searchByID(h.getMediaId());
									if (media != null) {
										title = media.getTitle();
									}
									
									info1.add(title);
									info1.add(Long.toString(h.getHoldUntilDate().getTime()));
									info1.add(h.getStatus().name());							
								}
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_HOLDS, Status.SUCCESS, info1);
								writerToClient.writeObject(messageToClient); 
							
							} else if (messageFromClient.getAction() == Action.CANCEL_HOLD) {
								ArrayList<String> infoIn = messageFromClient.getInfo();
								int holdId = Integer.parseInt(infoIn.get(0));
								
								boolean success = holdRepository.cancelHold(holdId, inventory, account.getId());
								
								ArrayList<String> infoOut = new ArrayList<>();
								Status status;
								
								if (success) {
									infoOut.add("Hold cancelled.");
									status = Status.SUCCESS;
									System.out.println("SERVER: Hold " + holdId + " succesfully cancelled.");
								} else {
									infoOut.add("Hold not found!");
									status = Status.FAILURE;
									System.out.println("SERVER: Hold " + holdId + " was not found!");
								}
								
								Message msg = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.CANCEL_HOLD, status, infoOut);
								writerToClient.writeObject(msg);
							} else if (messageFromClient.getAction() == Action.GET_PROFILE) {
							
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.GET_PROFILE, Status.SUCCESS, info);
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
								appendLog(loggingWriter, "Successfully edited their profile");
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.SET_PROFILE, Status.SUCCESS, null);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.ADD_BOOK) {
								ArrayList<String> newBookAttr = messageFromClient.getInfo();
								int newQuant = Integer.parseInt(newBookAttr.get(5));
								double newDd = Double.parseDouble(newBookAttr.getLast());
								inventory.addMedia(new Book(newBookAttr.get(1), newBookAttr.get(3), newBookAttr.get(4), newQuant, newQuant, newBookAttr.get(2), newDd, newBookAttr.get(0)));
								appendLog(loggingWriter, "Successfully added a new book, " + newBookAttr.get(1) + " by " + newBookAttr.get(2));
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.ADD_BOOK, Status.SUCCESS, info);
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
								appendLog(loggingWriter, "Successfully edited a book, " + newBookAttr.get(2) + " by " + newBookAttr.get(3));
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.EDIT_BOOK, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.DELETE_BOOK || messageFromClient.getAction() == Action.DELETE_DVD || messageFromClient.getAction() == Action.DELETE_GAME) {
								int id = Integer.parseInt(messageFromClient.getInfo().getFirst());
								boolean found = false;
								for (int i = 0; i < inventory.getMediaItems().size(); i += 1) {
									if (inventory.getMediaItems().get(i).getId() == id) {
										found = true;
										Media toBeDeleted = inventory.getMediaItems().get(i);
										appendLog(loggingWriter, "Successfully deleted a " + messageFromClient.getAction().toString().split("_")[1] + ", " + toBeDeleted.getTitle() + (messageFromClient.getAction() == Action.DELETE_BOOK ? " by " + ((Book) toBeDeleted).getAuthor() : ""));
										inventory.getMediaItems().remove(i);
										messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), messageFromClient.getAction(), Status.SUCCESS, info);
									}
								}
								if (!found) {
									appendLog(loggingWriter, "Failed to delete media with an ID of " + id);
									messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), messageFromClient.getAction(), Status.FAILURE, info);
								}
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.ADD_DVD) {
								ArrayList<String> newDvdAttr = messageFromClient.getInfo();
								int newQuant = Integer.parseInt(newDvdAttr.get(3));
								Rating newRating = stringToRating(newDvdAttr.get(4));
								int newRuntime = Integer.parseInt(newDvdAttr.get(5));
								inventory.addMedia(new DVD(newDvdAttr.get(0), newDvdAttr.get(1), newDvdAttr.get(2), newQuant, newQuant, newRating, newRuntime));
								appendLog(loggingWriter, "Successfully added a new DVD, " + newDvdAttr.get(0));
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.ADD_DVD, Status.SUCCESS, info);
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
								appendLog(loggingWriter, "Successfully edited a DVD, " + editDvd.getTitle());
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.EDIT_DVD, Status.SUCCESS, info);
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
								appendLog(loggingWriter, "Successfully added a new board game, " + newGameAttr.get(0));
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.ADD_GAME, Status.SUCCESS, info);
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
								appendLog(loggingWriter, "Successfully edited a board game, " + newGameAttr.get(1));
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.EDIT_GAME, Status.SUCCESS, info);
								addInventoryToInfoAdmin(inventory, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.LOGOUT) {
								inventory.saveInventoryToFile(library.getId() == 0 ? "ollinv" : "oblinv");
								String fullName = account.getFullName();
								account = null;
								info.add(library.getName());
								for (Media media : inventory.getMediaItems()) {
									if (media instanceof Book) {
										addBookToInfo(info, (Book) media, true);
									} else if (media instanceof DVD) {
										addDVDToInfo(info, (DVD) media, true);
									} else if (media instanceof BoardGame) {
										addBoardGameToInfo(info, (BoardGame) media, true);
									} else {
										System.out.println("Media of unexpected/unknown type found in inventory\n");
									}
								}
								appendLog(loggingWriter, "Successfully logged out");
								loggingWriter.close();
								messageToClient = new Message(Type.RESPONSE, messageFromClient.getId(), Action.LOGOUT, Status.SUCCESS, info);
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
	
	private static void appendLog(FileWriter loggingFile, String log) {
		try {
			loggingFile.write(new Date() + " - " + log + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
