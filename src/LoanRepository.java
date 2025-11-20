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
	
	public Loan checkoutMedia(int mediaId, int memberId, Date due) {
		Loan loan = new Loan(mediaId, memberId, new Date(), due);
		history.add(loan);
		numLoans++;
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
		
	}
	
}