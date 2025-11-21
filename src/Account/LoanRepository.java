package Account;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;


public class LoanRepository {
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
		
		Loan loan = new Loan(mediaId, memberId, new Date(), dueDate);
		history.add(loan);
		numLoans++;
		
		if (member != null) {
			member.getLoans().add(loan);
		}
		
		System.out.println("Checkout successful for member " + memberId +
							". \nTotal loans: " + (activeLoans + 1) + " loans.");
		return loan;
	}
	
	public boolean returnMedia(int mediaId, int memberId) {
		for (Loan loan : history) {
			if (loan.getMediaId() == mediaId && 
				loan.getMemberId() == memberId && 
				loan.getReturnedDate() == null) {
				
				loan.setReturnDate(new Date());
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
				writer.write(loan.getMediaId() + ", " +
							 loan.getMemberId() + ", " + 
							 loan.getCheckoutDate() + ", " +
							 loan.getDueDate() + ", " +
							 (loan.getReturnedDate() == null ? -1 : loan.getReturnedDate() + "\n"));
			}
			writer.close();
			System.out.println("Saved loan history.");
			
		} catch (IOException e) {
			System.out.println("Error saving loan: " + e.getMessage());
		}
	}
	
	public void loadLoansFromFile(String filename) {
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
				if (str.length != 5) {
					continue;
				}
				
				int mediaId = Integer.parseInt(str[0]);
				int memberId = Integer.parseInt(str[1]);
				long checkoutMillis = Long.parseLong(str[2]);
				long dueMillis = Long.parseLong(str[3]);
				long returnMillis = Long.parseLong(str[4]);
				
				boolean alreadyExists = false;
				for (Loan old : history) {
					if (old.getMediaId() == mediaId &&
						old.getMemberId() == memberId &&
						old.getCheckoutDate().getTime() == checkoutMillis) {
						alreadyExists = true;
						break;
					}
				}
				
				if (alreadyExists) {
					continue;
				}
				
				Loan loan = new Loan(mediaId, memberId, new Date(checkoutMillis), new Date(dueMillis));
				if (returnMillis != -1) {
					loan.setReturnDate(new Date(returnMillis));
				}
				
				history.add(loan);
				numLoans++;
			}
			
			sc.close();
			
		} catch (Exception e) {
			System.out.println("Error loading loans: " + e.getMessage());
		}
	}
	
}