package account;
import java.util.Date;
import java.util.ArrayList;

public class Member extends Account{
	private ArrayList<Loan> loans;
	private ArrayList<Hold> holds;
	
	public Member(String newEmail, String newPassword,
				  String newFullName, Date newBirthday) {
		super(newEmail, newPassword, newFullName, newBirthday);
		setPermission(Permission.MEMBER);	
		this.loans = new ArrayList<>();
		this.holds = new ArrayList<>();
	}
	
	public ArrayList<Loan> getLoans() {
		return loans;
	}
	
	public ArrayList<Hold> getHolds() {
		return holds;
	}
	
	public int getActiveLoanCount() {
		int count = 0;
		for (Loan loan : loans) {
			if (loan.getReturnedDate() == null) {
				count++;
			}
		}
		return count;
	}
	
	public void makePayment(Double amount, int ccNumber, String expDate, int csv) {
	System.out.print("[Payment made: $" + amount +
					 " using CC: " + ccNumber +
					 " Exp: " + expDate +
					 " CSV: " + csv + "]");
}
	
	@Override
	public String toString() {
		return "[MEMBER] " + getFullName() + " (" + getEmail() + ")"; 
	}
}