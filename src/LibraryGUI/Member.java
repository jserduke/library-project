package LibraryGUI;


public class Member {
	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String gender;

	public Member(int id, String firstName, String lastName,
               String phone, String email, String gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.gender = gender;
	}

	public int getId() { return id; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getPhone() { return phone; }
	public String getEmail() { return email; }
	public String getGender() { return gender; }

	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setPhone(String phone) { this.phone = phone; }
	public void setEmail(String email) { this.email = email; }
	public void setGender(String gender) { this.gender = gender; }
}

