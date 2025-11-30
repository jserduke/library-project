package client;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import message.*;

public class Client {
	public static void main(String[] args) {
		// COMMENTED OUT FOR NOW FOR MORE EFFICENT TESTING
		// Scanner scanner = new Scanner(System.in);
		// System.out.print("server IPv4 address: ");
		// String serverAddress = scanner.nextLine();
		// System.out.print("server port number (56789): ");
		// int serverPort = Integer.parseInt(scanner.nextLine());
		
		// CHANGE IP ADDRESS HERE IN ACCORDANCE WITH YOUR OWN SERVER-CLIENT CONFIGURATION
		try (Socket socket = new Socket("localhost", 56789)) {
			ObjectOutputStream writerToServer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream readerFromServer = new ObjectInputStream(socket.getInputStream());
			//JFrame frame = new JFrame("Home Page");
			ResponseHandler responseHandler = new ResponseHandler(readerFromServer, writerToServer);
			// GUIPreparer guiPreparer = new GUIPreparer(writerToServer, responseHandler);
			// responseHandler.setGuiPreparer(guiPreparer);
			Thread responseThread = new Thread(responseHandler);
			responseThread.start();
//			Message initMessage = new Message(0, Type.REQUEST, -1, message.Action.GET_DASHBOARD, Status.PENDING, null);
			ArrayList<String> initInfo = new ArrayList<String>();
			String libraryName = JOptionPane.showInputDialog(null, "Input the name of the library\n(\"Our Little Library\" or \"OBL\"):");
			initInfo.add(libraryName);
			Message initMessage = new Message(0, Type.REQUEST, -1, message.Action.GET_DASHBOARD, Status.PENDING, initInfo);
			responseHandler.setRequestIdExpected(initMessage.getId());
			writerToServer.writeObject(initMessage);
			// guiPreparer.createHomePageNotLoggedIn(frame);
			
			// Without this while loop, socket seems to go out of scope and close, breaking everything else
			// There's probably a better way to fix this but I'm just not sure of what it is
			while (true) {	
			}
			
			// OLD TESTING WITH CONSOLE (TEXT) INTERFACE INVOLVING A DIFFERENT THREAD TO HANDLE EACH EXPECTED RESPONSE
			// ULTIMATELY UNSUCCESSFUL BUT I STILL FIND REFERENCING IT USEFUL SOMETIMES
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
