package account;
import java.util.Date;

public abstract class Account {
	private int id;
	private String email;
	private String password;
	private String fullName;
	private Date birthday;
	private Permission permission;
	
	
	public Account (String email, String password, String fullName, Date birthday) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.birthday = birthday;
	}
	
	public void setId(int newId) {
		this.id = newId;
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
	
	public boolean checkPassword(String inputPW) {
		return this.password.equals(inputPW);
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
	
	@SuppressWarnings("deprecation")
	public void setBirthday(int month, int day, int year) {
		this.birthday = new Date(year - 1900, month - 1, day);
	}
	
	public Permission getPermission() {
		return permission;
	}
	
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	@Override
	public String toString() {
		return "[" + permission + "]" + fullName + " (" + email + ")";
	} 
	
}
