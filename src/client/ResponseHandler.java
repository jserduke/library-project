package client;
import java.io.*;
import javax.swing.*;

import gui.WelcomeDashboardFrame;
import message.Action;
import message.Message;

public class ResponseHandler implements Runnable {
		private final ObjectInputStream responseReader;
		private final ObjectOutputStream requestWriter;
		// private GUIPreparer guiPreparer;
		private JFrame oldFrame;
		private int requestIdExpected;
		
		public ResponseHandler(ObjectInputStream responseReader, ObjectOutputStream requestWriter) {
			this.responseReader = responseReader;
			this.requestWriter = requestWriter;
			// this.guiPreparer = null;
			this.oldFrame = null;
			this.requestIdExpected = -1;
		}
		
		// GUIPreparer AND ResponseHandler BOTH REFERENCE EACH OTHER SO ONE'S REFERENCE TO THE OTHER NEEDS TO BE SET AFTER THE OTHER'S CREATION
		// PROBABLY SHOULD NOT BE CHANGED AFTER INITIALLY SET
		/*
		public void setGuiPreparer(GUIPreparer guiPreparer) {
			this.guiPreparer = guiPreparer;
		}
		*/
		
		// SET WINDOW TO BE EDITED, NULL IF NEW WINDOW WILL BE CREATED
		public void setOldFrame(JFrame oldFrame) {
			this.oldFrame = oldFrame;
		}
		
		// LATEST REQUEST THAT RESPONSE SHOULD BE IN SERVICE OF FULFILLING
		public void setRequestIdExpected(int requestIdExpected) {
			this.requestIdExpected = requestIdExpected;
		}
		
		public void run() {
			try {
				while (true) {
					Message response = (Message) responseReader.readObject();
					System.out.println(response);
					// IF RESPONSE CAME IN OUT OF ORDER, IGNORE IT
					if (response.getRequestId() != requestIdExpected) {
						System.out.println("unexpected object was read (expected: " + requestIdExpected + " | actual: " + response.getRequestId() + ")");
						// response = (Message) responseReader.readObject();
					} else {
						switch (response.getAction()) {
							case Action.GET_DASHBOARD:
								SwingUtilities.invokeLater(() -> new WelcomeDashboardFrame(requestWriter, this, response.getInfo()).setVisible(true));
								break;
							case Action.GET_SEARCH:
								((WelcomeDashboardFrame) oldFrame).reloadResults(response.getInfo());
								break;
							case Action.LOGIN:
								// guiPreparer.updateHomePageToLoggedIn(frame, response);
								break;
							case Action.CHECKOUT:
								// WHEN RESPONSE RECEIVED, JUST LET USER KNOW THAT CHECKOUT WAS SUCCESSFUL
								JOptionPane.showMessageDialog(oldFrame, "Your checkout was successfully made!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
								break;
							case Action.GET_CHECKOUTS:
								// guiPreparer.showCheckoutHistory(new JFrame("Checkout History"), response);
								break;
							// IF CASE HASN'T BEEN WRITTEN YET
							default:
								System.out.println("not ready yet");
								
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}