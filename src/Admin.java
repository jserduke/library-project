import java.util.Date;
import java.util.ArrayList;

public class Admin extends Account{
	public Admin(Integer id, String newEmail, String newPassword,
				  String newFullName, Date newBirthday) {
		super(id, newEmail, newPassword, newFullName, newBirthday);
		setPermission(Permission.ADMIN);
	}
	
	public void manageInventory() {
		System.out.println("[ADMIN] " + getFullName() + " is managing inventory...");
	}
	
	public ArrayList<Loan> viewHistory(int memberId, LoanRepository loanRep) {
		ArrayList<Loan> history = new ArrayList<>();
		
		for (Loan loan : loanRep.getHistory()) {
			if (loan.getMemberId() == memberId) {
				history.add(loan);
			}
		}
		return history;
	}
	
	public ArrayList<Loan> viewLoans(int memberId, LoanRepository loanRep) {
		ArrayList<Loan> loans = new ArrayList<>();
		for (Loan loan : loanRep.getHistory()) {
			if (loan.getMemberId() == memberId && loan.getReturnedDate() == null) {
				loans.add(loan);
			}
		}
		return loans;
	}
	
	@Override
	public String toString() {
		return "[ADMIN] " + getFullName() + " (" + getEmail() + ")"; 
	}
}