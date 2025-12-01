package accountTest;

import account.*;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {
	
	private Account account;
	private Date birthday;
	
	private static class TestAccount extends Account {
		public TestAccount(String email, String password, String fullName, Date birthday) {
			super(email, password, fullName, birthday);
		}
	}
	
	@BeforeEach
	void setUp() {
		birthday = new Date(96, 4, 16);
		account = new TestAccount("test@email.com", "test1234", "Jane Doe", birthday);
		account.setPermission(Permission.MEMBER);
		account.setId(516);
	}

	@Test
	void testConstructorAndGetters() {
		assertEquals("test@email.com", account.getEmail());
		assertEquals("Jane Doe", account.getFullName());
		assertEquals(birthday, account.getBirthday());
		assertEquals(516, account.getId());
	}
	
	@Test
	void testSetEmail() {
		account.setEmail("member@email.com");
		assertEquals("member@email.com", account.getEmail());
	}
	
	@Test
	void testValidateCorrectPW() {
		assertTrue(account.checkPassword("test1234"));
	}
	
	@Test
	void testValidateWrongPW() {
		assertFalse(account.checkPassword("pw1234"));
	}

	@Test
	void testSetPassword() {
		account.setPassword("password101");
		assertTrue(account.checkPassword("password101"));
	}
	
	@Test
	void testSetFullName() {
		account.setFullName("John Doe");
		assertEquals("John Doe", account.getFullName());
	}
	
	@Test
	void testSetBirthdayDate() {
		Date newDate = new Date(99, 9, 8);
		account.setBirthday(newDate);
		assertEquals(newDate, account.getBirthday());
	}
	
	@Test 
	void testSetPermission() {
		account.setPermission(Permission.ADMIN);
		assertEquals(Permission.ADMIN, account.getPermission());
	}
	
	@Test
	void testToString() {
		account.setPermission(Permission.MEMBER);
		String str = account.toString();
		
		assertTrue(str.contains("[MEMBER]"));
		assertTrue(str.contains("Jane Doe"));
		assertTrue(str.contains("(test@email.com)"));
	}
}
