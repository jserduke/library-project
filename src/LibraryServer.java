import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
				boolean isLoggedIn = false;
				try {
					while (true) {
						messageFromClient = (Message) readerFromClient.readObject();
						// I BELIEVE THAT THIS IS WHERE RequestHandler FACADE WILL BE CALLED
						// JUST KEEPING LOGIC RIGHT HERE TEMPORARILY FOR TESTING PURPOSES
						System.out.println(messageFromClient);
						// System.out.println("Is logged in? " + isLoggedIn);
						ArrayList<String> info = new ArrayList<String>();
						if (!isLoggedIn) {
							// System.out.println("In");
							if (messageFromClient.getAction() == Action.LOGIN) {
								isLoggedIn = true;
								info.add("Jason");
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGIN, Status.SUCCESS, info);
							} else {
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
							} else if (messageFromClient.getAction() == Action.LOGOUT) {
								messageToClient = new Message(0, Type.RESPONSE, messageFromClient.getId(), Action.LOGOUT, Status.SUCCESS, info);
								writerToClient.writeObject(messageToClient);
								
								writerToClient.close();
								readerFromClient.close();
								clientSocket.close();
								return;
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
}
