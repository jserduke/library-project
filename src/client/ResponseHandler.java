package client;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.Window;

import gui.*;
import gui.AdminPortalFrame.ManageInventoryPanel;
import message.Action;
import message.*;
import gui.LateFeesPanel;
import gui.HoldsPanel;

public class ResponseHandler implements Runnable {
		private final ObjectInputStream responseReader;
		private final ObjectOutputStream requestWriter;
		// private GUIPreparer guiPreparer;
		private JFrame oldFrame;
//		private Window oldFrame;
		private JFrame oldOldFrame;
		private JDialog oldDialog;
		private JPanel oldPanel;
		private HoldsPanel activeHoldsPanel;
		private LateFeesPanel activeLateFeesPanel;
		private int requestIdExpected;
		
		public ResponseHandler(ObjectInputStream responseReader, ObjectOutputStream requestWriter) {
			this.responseReader = responseReader;
			this.requestWriter = requestWriter;
			// this.guiPreparer = null;
			this.oldFrame = null;
			this.oldOldFrame = null;
			this.oldDialog = null;
			this.oldPanel = null;
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
//		public void setOldFrame(JFrame oldFrame) {
//			this.oldFrame = oldFrame;
//		}
		
		public void setOldFrame(JFrame frame) {
			System.out.println("old frame set");
			this.oldFrame = frame;
		}
		
//		public JFrame getOldFrame() {
//			return oldFrame;
//		}
		
		public Window getOldFrame() {
			return oldFrame;
		}
		
		public void setOldOldFrame() {
			System.out.println("old old frame set");
			this.oldOldFrame = oldFrame;
		}
		
		public void setOldDialog(JDialog oldDialog) {
			this.oldDialog = oldDialog;
		}
		
		public void setOldPanel(JPanel oldPanel) {
			this.oldPanel = oldPanel;
		}
		
		// LATEST REQUEST THAT RESPONSE SHOULD BE IN SERVICE OF FULFILLING
		public void setRequestIdExpected(int requestIdExpected) {
			// System.out.println()
			this.requestIdExpected = requestIdExpected;
		}
		
		public void setActiveHoldsPanel(HoldsPanel hp) {
			this.activeHoldsPanel = hp;	
		}
		
		public void setActiveLateFeesPanel(LateFeesPanel panel) {
			this.activeLateFeesPanel = panel;	
		}
		
		public void run() {
			try {
				while (true) {
					
					Message response = (Message) responseReader.readObject();
					System.out.println(response);
					System.out.println("EXPECTING requestId = " + response.getId());
					// IF RESPONSE CAME IN OUT OF ORDER, IGNORE IT
					if (response.getAction() != Action.CANCEL_HOLD && response.getRequestId() != requestIdExpected) {
						System.out.println("unexpected object was read (expected: " + requestIdExpected + " | actual: " + response.getRequestId() + ")");
						continue;
						// response = (Message) responseReader.readObject();
					} else {
						System.out.println("RECEIVED requestId = " + response.getRequestId());

						switch (response.getAction()) {
							case Action.GET_DASHBOARD:
//								SwingUtilities.invokeLater(() -> new WelcomeDashboardFrame(requestWriter, this, response.getInfo()).setVisible(true));
								if (oldFrame instanceof MemberPortalFrame || oldFrame instanceof AdminPortalFrame) {
									break;
								}
								
								SwingUtilities.invokeLater(() -> new WelcomeDashboardFrame(requestWriter, this, response.getInfo()).setVisible(true));
								break;
							case Action.GET_SEARCH:
								((WelcomeDashboardFrame) oldFrame).reloadResults(response.getInfo(), 0);
								break;
							case Action.REGISTER:
								if (response.getStatus() == Status.SUCCESS) {
//									JOptionPane.showMessageDialog(oldDialog, "Account created. You can now log in.");
									JOptionPane.showMessageDialog(oldDialog, response.getInfo().getFirst() + " account created. You can now log in.");
									oldDialog.dispose();
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldDialog, "Something went wrong :(\n" + response.getInfo().getFirst());
								}
								break;
							case Action.LOGIN:
								if (response.getStatus() == Status.SUCCESS) {
									if (response.getInfo().getFirst().equals("ADMIN")) {
										response.getInfo().removeFirst();
										JOptionPane.showMessageDialog(null, "Admin Login Successful! Welcome, " + response.getInfo().getFirst());
										response.getInfo().removeFirst();
										// GO TO ADMIN PORTAL
										(new AdminPortalFrame(requestWriter, this, response.getInfo())).setVisible(true);
										oldOldFrame.dispose();
										oldFrame.dispose();
									} else if (response.getInfo().getFirst().equals("MEMBER")) {
										response.getInfo().removeFirst();
										JOptionPane.showMessageDialog(null, "Member Login Successful! Welcome, " + response.getInfo().getFirst());
										
//										(new MemberPortalFrame(requestWriter, this, response.getInfo())).setVisible(true);
//										MemberPortalFrame f = new MemberPortalFrame(requestWriter, this, response.getInfo());
										MemberPortalFrame f = new MemberPortalFrame(requestWriter, this, new ArrayList<>(response.getInfo()));
										f.setVisible(true);
										setOldFrame(f);
										f.setVisible(true);
										
//										oldOldFrame.dispose();
//										oldFrame.dispose();
//										if (oldOldFrame != null) {
//											oldOldFrame.dispose();
//										}
										if (oldFrame != null && oldFrame != f) {
											oldFrame.dispose();
										}								
										
										ArrayList<String> dummyList = new ArrayList<>();
										Message info = new Message(message.Type.REQUEST, -1, message.Action.GET_DASHBOARD, Status.PENDING, dummyList);
										this.setOldFrame(f);
										this.setRequestIdExpected(info.getId());
										
										try {
											requestWriter.writeObject(info);
										} catch (IOException ex) {
											ex.printStackTrace();
										}
									}
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(null, "Login Failed :(");
								} else {
									JOptionPane.showMessageDialog(null, "Huh?");
								}
								break;
							case Action.CHECKOUT:
								// WHEN RESPONSE RECEIVED, JUST LET USER KNOW THAT CHECKOUT WAS SUCCESSFUL
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldFrame, "Your checkout was successfully made!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Your checkout failed", "Checkout", JOptionPane.ERROR_MESSAGE);
								}
								
								ArrayList<String> msg1 = new ArrayList<>();
								//Commenting our for now because this worked with dummy data
								Message refresh1 = new Message(message.Type.REQUEST, -1, message.Action.GET_CHECKOUTS, Status.PENDING, msg1);
								this.setRequestIdExpected(refresh1.getId());
								this.setOldFrame(oldFrame);
//								try {
//									requestWriter.writeObject(refresh1);
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
								
								requestWriter.writeObject(refresh1);
								
//								((MemberPortalFrame) oldFrame).reloadCatalog(msg, 2);
//								int loanStart1 = 2 + Integer.parseInt(msg.get(1)) * 8;
//								((MemberPortalFrame) oldFrame).reloadLoans(msg, loanStart1);
								
								break;
							case Action.RETURN:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldFrame, "Your return was successfully made!");
									
									ArrayList<String> msg = new ArrayList<>();
									Message refresh = new Message(message.Type.REQUEST, -1, message.Action.GET_CHECKOUTS, Status.PENDING, msg);
									this.setRequestIdExpected(refresh.getId());
									this.setOldFrame(oldFrame);
									try {
										requestWriter.writeObject(refresh);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									String msg = (response.getInfo() != null && !response.getInfo().isEmpty()
												  ? response.getInfo().getFirst() : "Return failed!");
									JOptionPane.showMessageDialog(oldDialog, msg);
								}
								break;
							case Action.GET_CHECKOUTS:
								// guiPreparer.showCheckoutHistory(new JFrame("Checkout History"), response);
								if (response.getStatus() == Status.SUCCESS) {			
									ArrayList<String> info = response.getInfo();
//									if (activeHoldsPanel != null) {
//										activeHoldsPanel.reload(info);
//									}
									if (oldFrame instanceof MemberPortalFrame portal) {
										portal.reloadLoans(info, 1);
									}
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Retrieving checkouts failed!");
								}
							
								break;
							case Action.PLACE_HOLD:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldFrame, "Hold placed!");
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Hold failed.");
								}
								break;
							case Action.GET_HOLDS:
								if (response.getStatus() == Status.SUCCESS) {
									ArrayList<String> info = response.getInfo();
									
									if (activeHoldsPanel != null) {
										System.out.println("option 1");
										activeHoldsPanel.reload(info);
									} else {
										System.out.println("option 2");
										if (oldFrame instanceof MemberPortalFrame) {
											System.out.println("option 2.1");
											MemberPortalFrame frame = (MemberPortalFrame) oldFrame;
											
											MemberAccountDialog dialog = new MemberAccountDialog(frame, frame.getRequestWriter(), this, info);
											this.oldDialog = dialog;
											this.oldFrame = null;
											this.requestIdExpected = -1;
											dialog.setVisible(true);
										}
									}
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Retrieving holds failed!");
								}
								break;
							case Action.CANCEL_HOLD:
								if (response.getStatus() == Status.SUCCESS) {		
									Window parent = (oldDialog != null ? oldDialog : oldFrame);
									
									JOptionPane.showMessageDialog(parent, "Hold cancelled!", "Success", JOptionPane.INFORMATION_MESSAGE);

										Message refresh = new Message(Type.REQUEST, -1, Action.GET_HOLDS, Status.PENDING, new ArrayList<>());
										
										setRequestIdExpected(refresh.getId());
										try {
											requestWriter.writeObject(refresh);
										} catch (IOException ex) {
											ex.printStackTrace();
										}

									System.out.println("SENDING GET_HOLDS (after cancel)");
								} else if (response.getStatus() == Status.FAILURE) {
									Window parent = (oldDialog != null ? oldDialog : oldFrame);
									JOptionPane.showMessageDialog(parent, "Failed to cancel hold.");
								}
								
								break;
							case Action.GET_FEES:
								if (response.getStatus() == Status.SUCCESS) {
									if (activeLateFeesPanel != null) {
										activeLateFeesPanel.reload(response.getInfo());
									} else {
										JOptionPane.showMessageDialog(oldFrame, "Fees retrieved, but no LateFeesPanel is active.");
									}
								} else {
									JOptionPane.showMessageDialog(oldFrame, "Retrieving late fees failed");
								}
								break;
							case Action.PAY_FEES:
								if (response.getStatus() == Status.SUCCESS) {
									Window parent = (oldDialog != null ? oldDialog : oldFrame);
									JOptionPane.showMessageDialog(parent, "Fee marked as paid.");
									
									ArrayList<String> list = new ArrayList<>();
									Message refresh = new Message(Type.REQUEST, -1, Action.GET_FEES, Status.PENDING, list);
									
									setRequestIdExpected(refresh.getId());
									requestWriter.writeObject(refresh);
								} else {
									Window parent = (oldDialog != null ? oldDialog : oldFrame);
									JOptionPane.showMessageDialog(parent, "Failed to mark fee as paid.");
								}
								break;
							case Action.GET_PROFILE:
								if (response.getStatus() == Status.SUCCESS) {
									((MemberPortalFrame) oldFrame).editAccount(requestWriter, this, response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Failed to retrieve profile information");
								}
								break;
							case Action.SET_PROFILE:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldFrame, "Your account was successfully updated!");
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "There was a problem with editing your profile.");
								}
								break;
							case Action.GET_SEARCH_MEMBER:
								((MemberPortalFrame) oldFrame).reloadCatalog(response.getInfo(), 0, false);
								break;
							case Action.ADD_BOOK:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The new book was successfully added!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with adding the new book.");
								}
								break;
							case Action.EDIT_BOOK:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected book was successfully edited!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with editing the book.");
								}
								break;
							case Action.DELETE_BOOK:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected book was successfully deleted!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with deleting the book.");
								}
								break;
							case Action.ADD_DVD:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The new DVD was successfully added!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with adding the new DVD.");
								}
								break;
							case Action.EDIT_DVD:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected DVD was successfully edited!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with editing the DVD.");
								}
								break;
							case Action.DELETE_DVD:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected DVD was successfully deleted!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with deleting the DVD.");
								}
								break;
							case Action.ADD_GAME:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The new board game was successfully added!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with adding the new board game.");
								}
								break;
							case Action.EDIT_GAME:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected board game was successfully edited!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with editing the board game.");
								}
								break;
							case Action.DELETE_GAME:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldPanel, "The selected board game was successfully deleted!");
									((ManageInventoryPanel) oldPanel).reloadAll(response.getInfo());
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldPanel, "There was a problem with deleting the board game.");
								}
								break;
							case Action.LOGOUT:
								if (response.getStatus() == Status.SUCCESS) {
									(new WelcomeDashboardFrame(requestWriter, this, response.getInfo())).setVisible(true); 
						        	oldFrame.dispose();
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Logout Failed?");
								}
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
		
		public void setOldWindow(Window w) {
			if (w instanceof JFrame frame) {
				this.oldFrame = frame;
			} else if (w instanceof JDialog dialog) {
				this.oldDialog = dialog;
			}
		}	
		
		
		
//		public void showHoldsDiaglog(ArrayList<String> info) {
//			int index = 0;
//			
//			int count = Integer.parseInt(info.get(index++));
//			String[] columns = {"Hold ID", "Media ID", "Title", "Placed", "Until"};
//			Object[][] data = new Object[count][columns.length];
//			
//			for (int i = 0; i < count; i++) {
//				int holdId = Integer.parseInt(info.get(index++));
//				int mediaId = Integer.parseInt(info.get(index++));
//				String title = info.get(index++);
//				long placedMillis = Long.parseLong(info.get(index++));
//				long untilMillis = Long.parseLong(info.get(index++));
//				int memberId = Integer.parseInt(info.get(index++));
//				
//				data[i][0] = holdId;
//				data[i][1] = mediaId;
//				data[i][2] = title;
//				data[i][3] = new java.util.Date(placedMillis);
//				data[i][4] = new java.util.Date(untilMillis);
//			}
//			
//			JTable table = new JTable(data, columns);
//			JScrollPane scroll = new JScrollPane(table);
//			
////			JDialog dialog = new JDialog(this, "My Holds", true);
//		}
		
	}