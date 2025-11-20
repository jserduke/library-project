package LibraryGUI;

public class User {
	
    public enum Role { 
    	USER, ADMIN 
    }

    private final String username; 
    private String password;
    private final Role role;
    private Integer memberId; 

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { 
    	return username; 
    }
    public String getPassword() { 
    	return password; 
    }
    public Role getRole() { 
    	return role; 
    }
    public Integer getMemberId() { 
    	return memberId; 
    }
    public void setMemberId(Integer memberId) { 
    	this.memberId = memberId; 
    }
    public void setPassword(String password) { 
    	this.password = password; 
    }
}
