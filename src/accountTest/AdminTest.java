package accountTest;

import account.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminTest {
	private Admin admin;
	private LoanRepository loanRepo;
	private Date date;
	
	@BeforeEach
	void setUp() {
		date = new Date(105, 9, 8);
		admin = new Admin("admin@email.com", "pw1234", "Iam Admin", date);
		
		loanRepo = new LoanRepository();
		
		loanRepo.getHistory().add(new Loan(001, 12, 222, new Date(), null));
		loanRepo.getHistory().add(new Loan(002, 13, 333, new Date(), new Date()));
		loanRepo.getHistory().add(new Loan(003, 13, 444, new Date(), null));
	}
	
	@Test
	void testConstructor() {
		assertEquals("admin@email.com", admin.getEmail());
		assertEquals("Iam Admin", admin.getFullName());
		assertEquals(date, admin.getBirthday());
	}
	
	@Test
	void testPermission() {
		assertEquals(Permission.ADMIN, admin.getPermission());
	}
	
	@Test
	void testViewHistoryNoResults() {
		ArrayList<Loan> history = admin.viewHistory(24, loanRepo);
		assertTrue(history.isEmpty());
	}
	
	@Test
	void testViewLoansNotReturned() {
		ArrayList<Loan> loans = admin.viewLoans(13, loanRepo);
		for (Loan loan : loans) {
			assertNull(loan.getReturnedDate(), "Returned loans must not appear.");
		}
	}

	@Test
	void testToString() {
		String str = admin.toString();
		assertTrue(str.contains("[ADMIN]"));
		assertTrue(str.contains("Iam Admin"));
		assertTrue(str.contains("(admin@email.com)"));
	}

}
