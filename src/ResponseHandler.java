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
		
		public void setGuiPreparer(GUIPreparer guiPreparer) {
			this.guiPreparer = guiPreparer;
		}
		
		public void setFrame(JFrame frame) {
			this.frame = frame;
		}
		
		public void setRequestIdExpected(int requestIdExpected) {
			this.requestIdExpected = requestIdExpected;
		}
		
		public void run() {
			try {
				while (true) {
					Message response = (Message) responseReader.readObject();
					System.out.println(response);
					if (response.getRequestId() != requestIdExpected) {
						System.out.println("unexpected object was read (expected: " + requestIdExpected + " | actual: " + response.getRequestId() + ")");
						response = (Message) responseReader.readObject();
					} else {
						switch (response.getAction()) {
							case Action.LOGIN:
								guiPreparer.updateHomePageToLoggedIn(frame, response);
								break;
							case Action.CHECKOUT:
								JOptionPane.showMessageDialog(frame, "Your checkout was successfully made!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
								break;
							case Action.GET_CHECKOUTS:
								guiPreparer.showCheckoutHistory(new JFrame("Checkout History"), response);
								break;
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