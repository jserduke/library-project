import java.util.Date;
import java.util.ArrayList;

public class Member extends Account{
	private ArrayList<Loan> loans;
	private ArrayList<Hold> holds;
	
	public Member(Integer id, String newEmail, String newPassword,
				  String newFullName, Date newBirthday) {
		super(id, newEmail, newPassword, newFullName, newBirthday);
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
	
	@Override
	public String toString() {
		return "[MEMBER] " + getFullName() + " (" + getEmail() + ")"; 
	}
}