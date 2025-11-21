import java.util.Date;

public class Member extends Account{
	public Member(Integer id, String newEmail, String newPassword,
				  String newFullName, Date newBirthday) {
		super(id, newEmail, newPassword, newFullName, newBirthday);
		setPermission(Permission.MEMBER);
	}
	
	@Override
	public String toString() {
		return "[MEMBER] " + getFullName() + " (" + getEmail() + ")"; 
	}
}