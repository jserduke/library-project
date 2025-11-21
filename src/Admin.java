import java.util.Date;

public class Admin extends Account{
	public Admin(Integer id, String newEmail, String newPassword,
				  String newFullName, Date newBirthday) {
		super(id, newEmail, newPassword, newFullName, newBirthday);
		setPermission(Permission.ADMIN);
	}
	
	@Override
	public String toString() {
		return "[ADMINN] " + getFullName() + " (" + getEmail() + ")"; 
	}
}