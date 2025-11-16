import java.io.*;
import javax.swing.*;

public class ResponseHandler implements Runnable {
		private final ObjectInputStream responseReader;
		private GUIPreparer guiPreparer;
		private JFrame frame;
		private int requestIdExpected;
		
		public ResponseHandler(ObjectInputStream responseReader) {
			this.responseReader = responseReader;
			this.guiPreparer = null;
			this.frame = null;
			this.requestIdExpected = -1;
		}
		
		// GUIPreparer AND ResponseHandler BOTH REFERENCE EACH OTHER SO ONE'S REFERENCE TO THE OTHER NEEDS TO BE SET AFTER THE OTHER'S CREATION
		// PROBABLY SHOULD NOT BE CHANGED AFTER INITIALLY SET
		public void setGuiPreparer(GUIPreparer guiPreparer) {
			this.guiPreparer = guiPreparer;
		}
		
		// SET WINDOW TO BE EDITED, NULL IF NEW WINDOW WILL BE CREATED
		public void setFrame(JFrame frame) {
			this.frame = frame;
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
						response = (Message) responseReader.readObject();
					} else {
						switch (response.getAction()) {
							case Action.LOGIN:
								guiPreparer.updateHomePageToLoggedIn(frame, response);
								break;
							case Action.CHECKOUT:
								// WHEN RESPONSE RECEIVED, JUST LET USER KNOW THAT CHECKOUT WAS SUCCESSFUL
								JOptionPane.showMessageDialog(frame, "Your checkout was successfully made!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
								break;
							case Action.GET_CHECKOUTS:
								guiPreparer.showCheckoutHistory(new JFrame("Checkout History"), response);
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