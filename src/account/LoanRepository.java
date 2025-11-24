package account;

import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import inventoryy.*;


public class LoanRepository {
	private int nextLoanId = 1001;
	private int numLoans;
	private int loanLimit;
	private ArrayList<Loan> history;
	
	public LoanRepository() {
		numLoans = 0;
		loanLimit = 5;
		history = new ArrayList<>();
	}
	
	public int getNumLoans() {
		return numLoans;
	}
	
	public int getLoanLimit() {
		return loanLimit;
	}
	
	public void setLoanLimit(int loanLimit) {
		this.loanLimit = loanLimit;
	}
	
	public ArrayList<Loan> getHistory() {
		return history;
	}
	
	public Loan checkoutMedia(int mediaId, int memberId, Date dueDate, Member member) {
		int activeLoans = 0;
		for (Loan loan : history) {
			if (loan.getMemberId() == memberId && loan.getReturnedDate() == null) {
				activeLoans++;
			}
		}
		if (activeLoans >= loanLimit) {
			System.out.println("Checkout denied. Member: " + memberId +
								" reached loan limit of " + loanLimit + " loans at a time.");
			return null;
		}
		
		Loan loan = new Loan(nextLoanId++, mediaId, memberId, new Date(), dueDate);
		history.add(loan);
		numLoans++;
		
		if (member != null) {
			member.getLoans().add(loan);
		}
		
		System.out.println("Checkout successful for member " + memberId +
							". \nTotal loans: " + (activeLoans + 1) + " loans.");
		return loan;
	}
	
	public boolean returnMedia(int mediaId, int memberId, Inventory inventory) {
		for (Loan loan : history) {
			if (loan.getMediaId() == mediaId && 
				loan.getMemberId() == memberId && 
				loan.getReturnedDate() == null) {
				
				loan.setReturnDate(new Date());
				
				ArrayList<Media> mediaList = inventory.searchByID(mediaId);
				if (mediaList != null && !mediaList.isEmpty()) {
					Media m = mediaList.get(0);
					m.setQuantityAvailable(m.getQuantityAvailable() + 1);
				}
				
				return true;
			}
		}
		return false;
	}
	
	public double calculateFees(int mediaId, int memberId) {
		//calculate fees a day after due date = $1.00
		for (Loan loan : history) {
			if (loan.getMediaId() == mediaId &&
				loan.getMemberId() == memberId &&
				loan.getReturnedDate() != null) {
				long diff = loan.getReturnedDate().getTime() - loan.getDueDate().getTime();
				
				if (diff <= 0) {
					return 0.0;
				}
				
				long oneWeekMillis = 1000L * 60 * 60 * 24 * 7;
				
				long weekLate = diff / oneWeekMillis;
				if (diff % oneWeekMillis != 0) {
					weekLate++;
				}
				
				return weekLate * 1.00;
			}
		}
		return 0.0;
	}
	
	public void saveLoansToFile(String filename) {
		File file = new File(filename);
		
		try {
			FileWriter writer = new FileWriter(file);
			for (Loan loan : history) {
//				Format was not good				
//				writer.write(loan.getLoanId() + ", " +
//							 loan.getMediaId() + ", " +
//							 loan.getMemberId() + ", " + 
//							 loan.getCheckoutDate() + ", " +
//							 loan.getDueDate() + ", " +
//							 (loan.getReturnedDate() == null ? -1 : loan.getReturnedDate() + "\n"));
				writer.write("LoadId=" + loan.getLoanId() + ", " +
							 "MediaId=" + loan.getMediaId() + ", " +
							 "MemberId=" + loan.getMemberId() + ", " +
							 "Checkout=" + loan.getCheckoutDate().getTime() + ", " +
							 "Due=" + loan.getDueDate().getTime() + ", " +
							 "Returned=" + (loan.getReturnedDate() == null ? -1 : loan.getReturnedDate().getTime()));
				writer.write("\n");
			}
			writer.close();
			System.out.println("Saved loan history.");
			
		} catch (IOException e) {
			System.out.println("Error saving loan: " + e.getMessage());
		}
	}
	
	public void loadLoansFromFile(String filename) {
		history.clear();
		
		try {
			File file = new File(filename);
			if (!file.exists()) {
				System.out.println(filename + " not found. No loans loaded.");
				return;
			}
			
			Scanner sc = new Scanner(file);
			
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.trim().isEmpty()) {
					continue;
				}
				
				String[] str = line.split(", ");
				
				int loanId = Integer.parseInt(str[0].split("=")[1]);
				int mediaId = Integer.parseInt(str[1].split("=")[1]);
				int memberId = Integer.parseInt(str[2].split("=")[1]);
				long checkoutMillis = Long.parseLong(str[3].split("=")[1]);
				long dueMillis = Long.parseLong(str[4].split("=")[1]);
				long returnMillis = Long.parseLong(str[5].split("=")[1]);
					
				boolean alreadyExists = false;
				for (Loan old : history) {
					if (old.getLoanId() == loanId &&
						old.getMediaId() == mediaId &&
						old.getMemberId() == memberId &&
						old.getCheckoutDate().getTime() == checkoutMillis) {
						alreadyExists = true;
						break;
					}
				}
					
				if (alreadyExists) {
					continue;
				}
					
				Loan loan = new Loan(loanId, mediaId, memberId, new Date(checkoutMillis), new Date(dueMillis));
					
				if (returnMillis != -1) {
					loan.setReturnDate(new Date(returnMillis));
				}
				if (loanId >= nextLoanId) {
					nextLoanId = loanId + 1;
				}
					
				history.add(loan);
//					numLoans++;
				
			}
			
			sc.close();
			
		} catch (Exception e) {
			System.out.println("Error loading loans: " + e.getMessage());
		}
	}
}
