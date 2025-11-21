package gui;

import java.time.LocalDate;

public class Member {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phone;
    private String email;

    public Member(int id, String firstName, String lastName, LocalDate birthday, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
    }

    public int getId() { 
    	return id; 
    }
    public String getFirstName() { 
    	return firstName; 
    }
    public String getLastName() { 
    	return lastName; 
    }
    public LocalDate getBirthday() { 
    	return birthday; 
    }
    public String getPhone() { 
    	return phone; 
    }
    public String getEmail() { 
    	return email; 
    }

    public void setFirstName(String firstName) { 
    	this.firstName = firstName; 
    }
    public void setLastName(String lastName) { 
    	this.lastName = lastName; 
    }
    public void setBirthday(LocalDate birthday) { 
    	this.birthday = birthday; 
    }
    public void setPhone(String phone) { 
    	this.phone = phone; 
    }
    public void setEmail(String email) { 
    	this.email = email; 
    }
}
