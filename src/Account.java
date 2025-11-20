import java.util.Calendar;
import java.util.Date;

public class Account {
	private int id;
	private String email;
	private String password;
	private String fullName;
	private Date birthday;
	private Permission permission;
	
	public Account(int id, String email, String password, String fullName, Date birthday, Permission permission) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.birthday = birthday;
		this.permission = permission;
	}
	
	public Account (int id, String email, String password, String fullName, Date birthday) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.birthday = birthday;
	}
	
	public int getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String pw) {
		this.password = pw;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String name) {
		this.fullName = name;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(int Month, int Day, int Year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Month - 1);
		cal.set(Calendar.DAY_OF_MONTH, Day);
		cal.set(Calendar.YEAR, Year);
		this.birthday = cal.getTime();
	}
	
	public Permission getPermission() {
		return permission;
	}
	
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	public void makePayment(Double amount, int ccNumber, String expDate, int csv) {
		System.out.print("[Payment made: $" + amount +
						 " using CC: " + ccNumber +
						 " Exp: " + expDate +
						 " CSV: " + csv + "]");
	}
	
	@Override
	public String toString() {
		return "[" + permission + "]" + fullName + " (" + email + ")";
	} 
	
}
