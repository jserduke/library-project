import java.util.*;
import java.util.ArrayList;

public class AccountsDirectory {
	private int numAccounts;
	private ArrayList<String> adminEmails;
	private ArrayList<String> memberEmails;
	private ArrayList<Account> accounts;
	
	public AccountsDirectory() {
		this.numAccounts = 0;
		this.adminEmails = new ArrayList<String>();
		this.memberEmails = new ArrayList<String>();
		this.accounts = new ArrayList<Account>();
	}
	
	public int getNumAccounts() {
		return numAccounts;
	}
	
	public ArrayList<String> getAdminEmails() {
		return adminEmails;
	}
	
	public ArrayList<String> getMemberEmails() {
		return memberEmails;
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public Account findAccount(Permission permissionLevel, String name, Integer id) {
		for (Account a : accounts) {
			if (permissionLevel != null && a.getPermission() != permissionLevel) {
				continue;
			}
			
			if (id != null) {
				if (a.getId() == id) {
					return a;
				}
				continue;
			}
			
			else if (name != null && !name.isBlank()) {
				if (a.getFullName().equalsIgnoreCase(name)) {
					return a;
				}
			}
		}
		return null;
	} 
	
	public void registerNewAccount(Permission permission,
								   Integer id,
								   String newEmail,
								   String newPassword,
								   String newFullName,
								   Date newBirthday) {
		if (findAccountByEmail(newEmail) != null) {
			System.out.println("ERROR: Email already registered: " + newEmail);
			return;
		}
		
		int newId = numAccounts + 1;
		
		Account newAccount;
		
		if (permission == Permission.ADMIN) {
			newAccount = new Admin(newId, newEmail, newPassword,
								   newFullName, newBirthday);
			adminEmails.add(newEmail);
		}
		
		else {
			newAccount = new Member(newId, newEmail, newPassword,
								   newFullName, newBirthday);
			memberEmails.add(newEmail);
		}
		
		accounts.add(newAccount);
		numAccounts++;
	}
	
	public Account login(String inputEmail, String inputPassword) {
		for (Account a : accounts) {
			if (a.getEmail().equalsIgnoreCase(inputEmail) &&
				a.getPassword().equals(inputPassword)) {
				return a;
			}
		}
		return null;
	}
	
	private Account findAccountByEmail(String email) {
		for (Account a : accounts) {
			if (a.getEmail().equalsIgnoreCase(email)) {
				return a;
			}
		}
		return null;
	}
}