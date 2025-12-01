package accountTest;

import account.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
    void testMemberConstructor() {
		Date birthday = new Date(92, 4, 23);
        Member m = new Member("mail@mail.com", "pw", "John Doe", birthday);
        assertEquals(Permission.MEMBER, m.getPermission());
        assertEquals("John Doe", m.getFullName());
        assertEquals("mail@mail.com", m.getEmail());
        assertEquals(birthday, m.getBirthday());
        
    }

    @Test
    void testGetLoan() {
        Member m = new Member("test@test.com", "pw", "Tester", new Date());
        
        assertNotNull(m.getLoans());
        assertEquals(0, m.getLoans().size());

        Loan l = new Loan(1, 99, 10, new Date(), new Date());
        m.getLoans().add(l);

        assertEquals(1, m.getActiveLoanCount());
    }

    @Test
    void testGetHold() {
    	Member m = new Member("test@test.com", "pw", "Tester", new Date());
    	
    	assertNotNull(m.getHolds());
        assertEquals(0, m.getHolds().size());

        Hold h = new Hold(1, 25, 10, new Date(), new Date(), HoldStatus.ACTIVE);
        m.getHolds().add(h);

        assertEquals(1, m.getHolds().size());
    }
    
    @Test
    void testGetActiveLoanCount() {
    	Member m = new Member("test@test.com", "pw", "Tester", new Date());
    	
    	Loan active1 = new Loan(2, 100, 10, new Date(), new Date());
    	Loan active2 = new Loan(2, 101, 10, new Date(), new Date());
    	Loan returned = new Loan(2, 101, 10, new Date(), new Date());
    	returned.setReturnDate(new Date());
    	
    	m.getLoans().add(active1);
    	m.getLoans().add(active2);
    	m.getLoans().add(returned);
    	
    	assertEquals(2, m.getActiveLoanCount());
    }
    
    @Test
    void testMakePaymentOutput() {
    	Member m = new Member("test@test.com", "pw", "Tester", new Date());
    	
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(out));
    	
    	m.makePayment(25.75, 123456789, "12/27", 425);
    	
    	System.setOut(System.out);
    	
    	String str = out.toString().trim();
    	
    	assertTrue(str.contains("[Payment made: $25.75"));
    	assertTrue(str.contains("using CC: 123456789"));
    	assertTrue(str.contains("Exp: 12/27"));
    	assertTrue(str.contains("CSV: 425]"));
    }
    
    @Test
    void testToString() {
    	Member m = new Member("mail@mail.com", "pw", "John Doe", new Date());
    	String txt = m.toString();
    	
     	assertTrue(txt.contains("[MEMBER]"));
    	assertTrue(txt.contains("John Doe"));
    	assertTrue(txt.contains("mail@mail.com"));
    }

}
