package accountTest;

import account.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
    void testMemberConstructor() {
        Member m = new Member("mail@mail.com", "pw", "John Doe", new Date());
        assertEquals(Permission.MEMBER, m.getPermission());
        assertEquals("John Doe", m.getFullName());
    }

    @Test
    void testAddLoan() {
        Member m = new Member("m@m.com", "pw", "M", new Date());

        Loan l = new Loan(1, 99, 10, new Date(), new Date());
        m.getLoans().add(l);

        assertEquals(1, m.getActiveLoanCount());
    }

    @Test
    void testAddHold() {
        Member m = new Member("m@m.com", "pw", "M", new Date());

        Hold h = new Hold(1, 99, 10, new Date(), new Date(), HoldStatus.ACTIVE);
        m.getHolds().add(h);

        assertEquals(1, m.getHolds().size());
    }

}
