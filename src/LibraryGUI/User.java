package LibraryGUI;


 // Simple user account used for authentication and to link to a Member record.
 
public class User {

    public enum Role { 
    	ADMIN, 
    	USER 
    }

    private String username;
    private String password;
    private Role role;
    /** Optional link to a Member.id; may be null until created. */
    private Integer memberId;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = (role == null ? Role.USER : role);
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
}
