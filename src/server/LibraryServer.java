package server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

import account.*;
import message.*;
import inventory.*;


public class LibraryServer {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(56789);
			server.setReuseAddress(true);
			
			InetAddress serverAddress = InetAddress.getLocalHost();
			System.out.println("server IPv4 address: " + serverAddress.getHostAddress());
			
			while (true) {
				Socket client = server.accept();
				System.out.println("new client IPv4 address: " + client.getInetAddress().getHostAddress());
				ClientHandler clientHandler = new ClientHandler(client);
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
		
		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		
		public void run() {
			ObjectOutputStream writerToClient = null;
			ObjectInputStream readerFromClient = null;
			try {
				writerToClient = new ObjectOutputStream(clientSocket.getOutputStream());
				readerFromClient = new ObjectInputStream(clientSocket.getInputStream());
				Message messageFromClient = null, messageToClient = null;
				
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
							messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_DASHBOARD, Status.SUCCESS, info);
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
							// TODO:
							// inventory = Library.getInventory();
							// for item in inventory:
							// add each piece of info
							// addFullInventoryDummyData(info);
							
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
							// TODO:
							// library.searchByName
							// for item in results
							// add piece of info
							/*
							info.add("Book");
							info.add("4");
							info.add("Booksmart");
							info.add("B. Smart");
							info.add("1234566789");
							info.add("Idk");
							info.add("4");
							info.add("0");
							*/
							
							writerToClient.writeObject(messageToClient);
						} else if (account == null) {
							if (messageFromClient.getAction() == Action.LOGIN) {
								account = accountsDirectory.login(messageFromClient.getInfo().getFirst(), messageFromClient.getInfo().getLast());
								if (account == null) {
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.FAILURE, null);
								} else {
									info.add(account instanceof Admin ? "ADMIN" : "MEMBER");
									info.add(account.getFullName());
									if (info.getFirst().equals("MEMBER")) {
										// inventory info
										info.add("2"); // number of inventory items returned
										addFullInventoryDummyData(info);
										
										// loan info
										info.add("3");
										info.add("BOOK");
										info.add("3");
										info.add("TITLE");
										info.add("2025-11-15");
										info.add("2025-12-01");
										info.add("4");
										info.add("");
									} else if (info.getFirst() == "ADMIN") {
								    	info.add("BOOK");
								    	info.add("1");
								    	info.add("1234567890");
								    	info.add("This Title");
								    	info.add("Author");
								    	info.add("Publishing House");
								    	info.add("Horror");
								    	info.add("4");
								    	info.add("2");
								    	
								    	info.add("DVD");
								    	info.add("2");
								    	info.add("This Movie");
								    	info.add("R");
								    	info.add("150");
								    	info.add("4");
								    	info.add("2");
								    	
								    	info.add("BOARD_GAME");
								    	info.add("3");
								    	info.add("Fun Game");
								    	info.add("12+");
								    	info.add("2-4");
								    	info.add("~120");
								    	info.add("5");
								    	info.add("5");
									}
									messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.SUCCESS, info);
								}
							} else if (messageFromClient.getAction() == Action.REGISTER) {
								Account newAccount = accountsDirectory.registerNewAccount(Permission.MEMBER,
										messageFromClient.getInfo().getFirst(), messageFromClient.getInfo().get(1),
										messageFromClient.getInfo().get(2), new Date(messageFromClient.getInfo().getLast()));
								if (newAccount != null) {
									System.out.println(newAccount);
									System.out.println(newAccount.getBirthday());
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
								info.add("Harry Potter");
								info.add("Mouse Paint");
								info.add("If You Give a Mouse a Cookie");
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.GET_CHECKOUTS, Status.SUCCESS, info);
								writerToClient.writeObject(messageToClient);
							} else if (messageFromClient.getAction() == Action.CHECKOUT) {
								info.add("Networking 101 successfully checked out!");
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.CHECKOUT, Status.SUCCESS, info);
								writerToClient.writeObject(messageToClient);
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
							}
							else if (messageFromClient.getAction() == Action.LOGOUT) {
								account = null;
								info.add("Our Little Library");
								addFullInventoryDummyData(info);
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
}
