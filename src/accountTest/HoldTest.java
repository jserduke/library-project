package accountTest;

import account.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HoldTest {
	private Hold hold;
	private Date placedDate;
	private Date untilDate;
	
	@BeforeEach
	void setUp() {
		placedDate = new Date(125, 10, 11);
		untilDate = new Date(125, 10, 21);
		
		hold = new Hold(
				2001,
				12,
				30,
				placedDate,
				untilDate,
				HoldStatus.ACTIVE
		);
	}
	
	@Test
	void testConstructorsAndGetters() {
		assertEquals(2001, hold.getHoldId());
		assertEquals(12, hold.getMediaId());
		assertEquals(30, hold.getMemberId());
		assertEquals(placedDate, hold.getDatePlaced());
		assertEquals(untilDate, hold.getHoldUntilDate());
		assertEquals(HoldStatus.ACTIVE, hold.getStatus());
	}
	
	@Test
	void testSetStatus() {
		hold.setStatus(HoldStatus.CANCELLED);
		assertEquals(HoldStatus.CANCELLED, hold.getStatus());
		
		hold.setStatus(HoldStatus.EXPIRED);
		assertEquals(HoldStatus.EXPIRED, hold.getStatus());
	}
	
	@Test
	void testToString() {
		String result = hold.toString();
		
		assertTrue(result.contains("mediaId="));
		assertTrue(result.contains("memberId="));
		assertTrue(result.contains("holdUntilDate="));
	}
	


}
