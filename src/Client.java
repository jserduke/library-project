import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Client {
	public static void main(String[] args) {
		// Scanner scanner = new Scanner(System.in);
		// System.out.print("server IPv4 address: ");
		// String serverAddress = scanner.nextLine();
		// System.out.print("server port number (56789): ");
		// int serverPort = Integer.parseInt(scanner.nextLine());
		try (Socket socket = new Socket("192.168.86.35", 56789)) {
			ObjectOutputStream writerToServer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream readerFromServer = new ObjectInputStream(socket.getInputStream());
			// Message messageToServer = null, messageFromServer = null;
			JFrame frame = new JFrame("Home Page");
			ResponseHandler responseHandler = new ResponseHandler(readerFromServer);
			GUIPreparer guiPreparer = new GUIPreparer(writerToServer, responseHandler);
			responseHandler.setGuiPreparer(guiPreparer);
			Thread responseThread = new Thread(responseHandler);
			responseThread.start();
			guiPreparer.createHomePageNotLoggedIn(frame);
			while (true) {
				
			}
			/*
			int choice = -1;
			while (true) {
				System.out.println("Enter 0 to LOGIN, 1 to GET CHECKOUTS, 2 to CHECKOUT, 3 to LOGOUT: ");
				choice = scanner.nextInt();
				switch (choice) {
					case 0:
						messageToServer = new Message(0, Type.REQUEST, -1, Action.LOGIN, Status.PENDING, "Please let me in");
						writerToServer.writeObject(messageToServer);
						// (new Thread(new ResponseHandler(readerFromServer, messageToServer.getId()))).start();
						if (responseThread == null || !responseThread.isAlive()) {
							responseHandler = new ResponseHandler(readerFromServer, messageToServer.getId());
							responseThread = new Thread(responseHandler, "login thread");
							responseThread.start();
						} else {
							responseHandler.setRequestIdExpected(messageToServer.getId());
							System.out.println("thread undead");
						}
						break;
					case 1:
						messageToServer = new Message(0, Type.REQUEST, -1, Action.GET_CHECKOUTS, Status.PENDING, "Give me records");
						writerToServer.writeObject(messageToServer);
						// (new Thread(new ResponseHandler(readerFromServer, messageToServer.getId()))).start();
						if (responseThread == null || !responseThread.isAlive()) {
							responseHandler = new ResponseHandler(readerFromServer, messageToServer.getId());
							responseThread = new Thread(responseHandler, "get checkouts thread");
							responseThread.start();
						} else {
							responseHandler.setRequestIdExpected(messageToServer.getId());
							System.out.println("thread undead");
						}
						break;
					case 2:
						messageToServer = new Message(0, Type.REQUEST, -1, Action.CHECKOUT, Status.PENDING, "Checkout please!");
						writerToServer.writeObject(messageToServer);
						// (new Thread(new ResponseHandler(readerFromServer, messageToServer.getId()))).start();
						if (responseThread == null || !responseThread.isAlive()) {
							responseHandler = new ResponseHandler(readerFromServer, messageToServer.getId());
							responseThread = new Thread(responseHandler, "checkout thread");
							responseThread.start();
						} else {
							responseHandler.setRequestIdExpected(messageToServer.getId());
							System.out.println("thread undead");
						}
						break;
					case 3:
						messageToServer = new Message(0, Type.REQUEST, -1, Action.LOGOUT, Status.PENDING, "All done");
						writerToServer.writeObject(messageToServer);
						// (new Thread(new ResponseHandler(readerFromServer, messageToServer.getId()))).start();
						if (responseThread == null || !responseThread.isAlive()) {
							responseHandler = new ResponseHandler(readerFromServer, messageToServer.getId());
							responseThread = new Thread(responseHandler, "logout thread");
							responseThread.start();
						} else {
							responseHandler.setRequestIdExpected(messageToServer.getId());
							System.out.println("thread undead");
						}
						// writerToServer.close();
						// readerFromServer.close();
						// socket.close();
						// System.exit(0);
						System.out.println("Done");
						scanner.nextInt();
						break;
					default:
						System.out.println("Waow");
				}
			}
			//	if (messageFromServer.getAction() == Action.LOGIN && messageFromServer.getStatus() == Status.SUCCESS) {
			//  } else {
			//  	System.out.println("I don't even know");
			//	}
			//
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
