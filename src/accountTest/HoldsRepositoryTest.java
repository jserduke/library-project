package accountTest;

import account.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HoldsRepositoryTest {
	private HoldsRepository holdsRepo;
	
	public class DummyMember extends Member {
		private ArrayList<Hold> holds = new ArrayList<>();
		public DummyMember(int id) {
			super("test@email.com", "wordy", "Member Test", new Date());
			setId(id);
		}
		
		@Override
		public ArrayList<Hold> getHolds() {
			return holds;
		}
	}	
	
	public class DummyMedia {
		int id;
		int qty;
		DummyMedia(int id, int qty) {
			this.id = id;
			this.qty = qty;
		}
	}
		
	public class DummyInventory {
		ArrayList<DummyMedia> list = new ArrayList<>();
			
		void add(DummyMedia m) {
			list.add(m);
		}
			
		void reduceQty(int mediaId) {
			for (DummyMedia m : list) {
				if (m.id == mediaId) {
					m.qty--;
				}
			}
		}
			
		void increaseQty(int mediaId) {
			for (DummyMedia m : list) {
				if (m.id == mediaId) {
					m.qty++;
				}
			}
		}
			
	}
		
	@BeforeEach
	void setUp() {
		holdsRepo = new HoldsRepository();
	}
	
	@Test
	void testPlaceHold() {
		DummyMember member = new DummyMember(321);
		DummyInventory inventory = new DummyInventory();
		inventory.add(new DummyMedia(24, 8));
		
		Date until = new Date(System.currentTimeMillis() + 86400000);
			
		Hold hold = holdsRepo.placeHold(24, 321, until, member, null);
			
		assertNotNull(hold);
		assertEquals(1, holdsRepo.getNumHolds());
		assertEquals(1, member.getHolds().size());
	
	}
	
	@Test
	void testCancelHold() {
		DummyMember member = new DummyMember(321);
		Hold hold = holdsRepo.placeHold(24, 321, new Date(System.currentTimeMillis() 
				+ 8644444), member, null);
		
		boolean result = holdsRepo.cancelHold(hold.getHoldId(), null, 321);
		
		assertTrue(result);
		assertEquals(HoldStatus.CANCELLED, hold.getStatus());
	}
	
	@Test
	void testCancelHoldNotFound() {
		
	}
	@Test
	void testSaveHoldToFile() throws Exception {
        Hold h = new Hold(
            3001, 101, 20,
            new Date(1000000),
            new Date(2000000),
            HoldStatus.ACTIVE
        );
        holdsRepo.getHolds().add(h);

        String filename = "golds_28_save.txt";
        holdsRepo.saveHoldToFile(filename);

        File file = new File(filename);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        file.delete();
    }
	
	 @Test
	    void testLoadHoldsFromFile() throws Exception {
		 String filename = "holds_45_test.txt";

	        String content =
	                "3005, 100, 50, " +
	                new Date(1000).getTime() + ", " +
	                new Date(2000).getTime() + "\n";

	        Files.write(new File(filename).toPath(), content.getBytes());

	        holdsRepo.loadHoldsFromFile(filename);

	        assertEquals(1, holdsRepo.getHolds().size());

	        Hold h = holdsRepo.getHolds().get(0);

	        assertEquals(3005, h.getHoldId());
	        assertEquals(100, h.getMediaId());
	        assertEquals(50, h.getMemberId());

	        new File(filename).delete();
	    }
	
	}
