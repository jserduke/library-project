package accountTest;

import account.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountsDirectoryTest {
	private AccountsDirectory directory;
	private Date birthday;

	@BeforeEach
	void setUp() {
		directory = new AccountsDirectory();
		birthday = new Date(90, 0, 1);
	}
	 
	@Test 
	void testInitiateState() {
		assertEquals(0, directory.getNumAccounts());
		assertTrue(directory.getAdminEmails().isEmpty());
		assertTrue(directory.getMemberEmails().isEmpty());
		assertTrue(directory.getAccounts().isEmpty());
	}
	
	@Test
	void testRegisterNewAdmin() {
		Account admin = directory.registerNewAccount(
				Permission.ADMIN,
				"admin@email.com",
				"password",
				"Admin User",
				birthday
		);
		
		assertNotNull(admin);
		assertEquals(1, directory.getNumAccounts());
		assertEquals(1, admin.getId());
		assertTrue(admin instanceof Admin);
		assertTrue(directory.getAdminEmails().contains("admin@email.com"));
		assertEquals(1, directory.getAccounts().size());
	}
	
	@Test
	void testRegisterNewMember() {
		Account member = directory.registerNewAccount(
				Permission.MEMBER,
				"member@email.com",
				"password",
				"Member User",
				birthday
		);
		
		assertNotNull(member);
		assertEquals(1, directory.getNumAccounts());
		assertEquals(1, member.getId());
		assertTrue(member instanceof Member);
		assertTrue(directory.getMemberEmails().contains("member@email.com"));
	}
	
	@Test 
	void testAssignIdIncrements() {
		Account acct1 = directory.registerNewAccount(
				Permission.MEMBER,
				"member1@email.com",
				"password",
				"Member User1",
				birthday
		);
		
		Account acct2 = directory.registerNewAccount(
				Permission.MEMBER,
				"member2@email.com",
				"password",
				"Member User2",
				birthday
		);
		
		assertEquals(1, acct1.getId());
		assertEquals(2, acct2.getId());
	}
	
	@Test
	void testRegisterDuplicateEmail() {
		directory.registerNewAccount(
				Permission.MEMBER,
				"dupe@email.com",
				"password1",
				"M. Ember",
				birthday
		);
		
		Account result = directory.registerNewAccount(
				Permission.MEMBER,
				"dupe@email.com",
				"password2",
				"Ember M",
				birthday
		);
		
		assertNull(result);
		assertEquals(1, directory.getNumAccounts());
	}
	
	@Test
	void testSuccessfulLogin() {
		directory.registerNewAccount(
				Permission.MEMBER,
				"login@email.com",
				"pw1234",
				"User Login",
				birthday
		);
		
		Account loggedIn = directory.login("login@email.com", "pw1234");
		assertNotNull(loggedIn);
		assertEquals("login@email.com", loggedIn.getEmail());
	}
	
	@Test 
	void testWrongLoginPW() {
		directory.registerNewAccount(
				Permission.MEMBER,
				"member@login.com",
				"1234",
				"User L",
				birthday
		);
		
		Account result = directory.login("member@login.com", "pw1234");
		assertNull(result);
	}
	
	@Test
	void testEmailNotFound() {
		Account result = directory.login("nothurr@email.com", "pass");
		assertNull(result);
	}
	
	@Test
	void testFindAcount() {
		directory.registerNewAccount(Permission.MEMBER, "newEmail@test.com", "newpw", "Knew Em", birthday);
		directory.registerNewAccount(Permission.MEMBER, "otherEmail@test.com", "newpw", "Ohld Em", birthday);
		
		Account acct = directory.login("otherEmail@test.com", "newpw");
		assertEquals("Ohld Em", acct.getFullName());
	}

}
