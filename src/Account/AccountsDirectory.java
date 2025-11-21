package Account;
import java.util.*;
import java.util.ArrayList;

public class AccountsDirectory {
	private int assignId;
	private int numAccounts;
	private ArrayList<String> adminEmails;
	private ArrayList<String> memberEmails;
	private ArrayList<Account> accounts;
	
	public AccountsDirectory() {
		this.numAccounts = 0;
		this.assignId = 1;
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
	
	public Account registerNewAccount(Permission permission,
								   String newEmail,
								   String newPassword,
								   String newFullName,
								   Date newBirthday) {
		for (Account acct : accounts) {
			if (acct.getEmail().equalsIgnoreCase(newEmail)) {
				System.out.println("ERROR: Email already exists.");
				return null;
			}
		}
		
		Account newAccount;
		
		if (permission == Permission.ADMIN) {
			newAccount = new Admin(newEmail, newPassword,
								   newFullName, newBirthday);
			adminEmails.add(newEmail);
		}
		
		else {
			newAccount = new Member(newEmail, newPassword,
								   newFullName, newBirthday);
			memberEmails.add(newEmail);
		}
		
		newAccount.setId(assignId);
		assignId++;
		
		accounts.add(newAccount);
		numAccounts++;
		
		return newAccount;
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
	
	private Account findAccount(Permission permissionLevel, String name, Integer id) {
		for (Account a : accounts) {
			if (a.getPermission() != permissionLevel) {
				continue;
			}
			
			if (id != null && a.getId() == id.intValue()) {
				return a;
			}
			
			if (id == null && name != null && a.getFullName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
}