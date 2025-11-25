package client;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

import gui.*;
import gui.AdminPortalFrame.ManageInventoryPanel;
import message.Action;
import message.*;
import library.*;

public class ResponseHandler implements Runnable {
		private final ObjectInputStream responseReader;
		private final ObjectOutputStream requestWriter;
		// private GUIPreparer guiPreparer;
		private JFrame oldFrame;
		private JFrame oldOldFrame;
		private JDialog oldDialog;
		private JPanel oldPanel;
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
		public void setOldFrame(JFrame oldFrame) {
			this.oldFrame = oldFrame;
		}
		
		public JFrame getOldFrame() {
			return oldFrame;
		}
		
		public void setOldOldFrame() {
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
										MemberPortalFrame f = new MemberPortalFrame(requestWriter, this, response.getInfo());
										f.setVisible(true);
										setOldFrame(f);
										f.setVisible(true);
										
//										oldOldFrame.dispose();
//										oldFrame.dispose();
										if (oldOldFrame != null) {
											oldOldFrame.dispose();
										}
										if (oldFrame != null && oldFrame != f) {
											oldFrame.dispose();
										}								
										
										ArrayList<String> dummyList = new ArrayList<>();
										Message dashboard = new Message(0, message.Type.REQUEST, -1, message.Action.GET_DASHBOARD, Status.PENDING, dummyList);
										this.setOldFrame(f);
										this.setRequestIdExpected(dashboard.getId());
										
										try {
											requestWriter.writeObject(dashboard);
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
								JOptionPane.showMessageDialog(oldFrame, "Your checkout was successfully made!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
								
								ArrayList<String> msg1 = new ArrayList<>();
								//Commenting our for now because this worked with dummy data
								Message refresh1 = new Message(0, message.Type.REQUEST, -1, message.Action.GET_CHECKOUTS, Status.PENDING, msg1);
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
									Message refresh = new Message(0, message.Type.REQUEST, -1, message.Action.GET_CHECKOUTS, Status.PENDING, msg);
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
//								Previous code that was working with dummy data
//								ArrayList<String> info = response.getInfo();
//								int loansStart = 1;
//								((MemberPortalFrame) oldFrame).reloadLoans(info, loansStart);
								
								ArrayList<String> info = response.getInfo();
								MemberPortalFrame frame = (MemberPortalFrame) oldFrame;
								
								int loanStart1 = 1;;
								((MemberPortalFrame)oldFrame).reloadLoans(info, loanStart1);
//								frame.reloadCatalog(info, 2);
//								frame.reloadLoans(info, loanStart1);
//							
								break;
							case Action.PLACE_HOLD:
								if (response.getStatus() == Status.SUCCESS) {
									JOptionPane.showMessageDialog(oldFrame, "Hold placed!");
								} else if (response.getStatus() == Status.FAILURE) {
									JOptionPane.showMessageDialog(oldFrame, "Hold failed.");
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
	}