package accountTest;

import account.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanTest {
	
	Loan loan;
	private Date checkoutDate;
	private Date dueDate;
	private Date returnDate;

	@BeforeEach
	void setUp() {
		checkoutDate = new Date(125, 10, 11);
		dueDate = new Date(125, 10, 21);
		returnDate = new Date(123, 10, 19);
		
		loan = new Loan(
				1001,
				25,
				10,
				checkoutDate,
				dueDate
		);
	}
	
	@Test
	void testConstructorAndGetters() {		
		assertEquals(1001, loan.getLoanId());
		assertEquals(25, loan.getMediaId());
		assertEquals(10, loan.getMemberId());
		assertEquals(checkoutDate, loan.getCheckoutDate());
		assertEquals(dueDate, loan.getDueDate());
		assertNull(loan.getReturnedDate());
	}
	
	@Test
	void testSetDueDate() {
		Loan l = new Loan(1, 25, 10, new Date(), new Date());
		Date due = new Date();
		l.setDateDue(due);;
		assertEquals(due, l.getDueDate());
	}
	
	@Test
	void testSetReturnDate() {
		Loan l = new Loan(1, 25, 10, new Date(), new Date());
		Date ret = new Date();
		l.setReturnDate(ret);
		assertEquals(ret, l.getReturnedDate());
	}
	
	@Test
	void testToString() {
		String result = loan.toString();
		
		assertTrue(result.contains("Loan Id:"));
		assertTrue(result.contains("Media Id:"));
		assertTrue(result.contains("Date checked out:"));
	}

}
