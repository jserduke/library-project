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
	
	//Still working on this class
}