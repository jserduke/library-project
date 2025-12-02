package accountTest;

import account.*;
import inventory.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;

public class LoanRepositoryTest {
	
	private LoanRepository loanRepo;
	
	@BeforeEach
	void loadRepo() {
		loanRepo = new LoanRepository();
	}
	
	public class DummyMedia extends Media{
		public DummyMedia(int id, int qty) {
			super(MediaType.BOOK, id, "Test title", "Test pub", "Test genre", qty, qty);
		}
		
	}
	
	public class DummyInventory extends Inventory {
		public DummyInventory() {
			super(new ArrayList<>());
		}
		
		void add(Media m) {
			getMediaItems().add(m);
		}
		
		public ArrayList<Media> searchById(int id) {
			ArrayList<Media> result = new ArrayList<>();
			
			for (Media m : getMediaItems()) {
				if (m.getId() == id) {
					result.add(m);
				}
			}
			return result.isEmpty() ? null : result;
		}
	}
	
	public class DummyMember extends Member {
		ArrayList<Loan> loans = new ArrayList<>();
		DummyMember(int id) {
			super("loan@test.com", "password", "Loner", new Date());
			setId(id);
		}
		@Override
		public ArrayList<Loan> getLoans() {
			return loans;
		}
	}
	
	@Test
	void testCheckoutMedia() {
		DummyMember member = new DummyMember(16);		
		DummyInventory inventory = new DummyInventory();
		
		DummyMedia media = new DummyMedia(1, 5);
		inventory.addMedia(media);
		
		Date dueDate = new Date(System.currentTimeMillis() + 86400000);
		
		Loan loan = loanRepo.checkoutMedia(1, 16, dueDate, member, inventory, null);
		
		assertNotNull(loan);
		assertEquals(1, loanRepo.getHistory().size());
		assertEquals(1, member.getLoans().size());		
		assertEquals(4, media.getQuantityAvailable());
	}
	
	@Test
	void testReturnMedia() {
		DummyMember member = new DummyMember(16);
		DummyInventory inventory = new DummyInventory();
		
		DummyMedia media = new DummyMedia(1, 5);
		inventory.addMedia(media);
		
		Loan loan = loanRepo.checkoutMedia(1, 16, new Date(), member, inventory, null);
		assertNotNull(loan);
		
		boolean b = loanRepo.returnMedia(1, 16, inventory);
		assertTrue(b);
		assertEquals(5, media.getQuantityAvailable());
	}
	
	@Test
	void testCalculateFees() {
		Loan loan = new Loan(1, 25, 16, new Date(), new Date());
		loan.setReturnDate(new Date());
		
		loanRepo.getHistory().add(loan);
		assertEquals(0.0, loanRepo.calculateFees(25, 16));
	}
	
	@Test
	void testSaveLoansToFile() throws IOException{
		String filename = "loans_16_test.txt";
		Loan loan = new Loan(3001, 25, 16, new Date(1000), new Date(2000));
		loan.setReturnDate(null);
		loanRepo.getHistory().add(loan);
		loanRepo.saveLoansToFile(filename);
		
		File file = new File(filename);
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
		
		file.delete();
	}
	
	@Test
	void testLoadLoansToFile() throws IOException {
		String filename = "loans_16_test.txt";
		
		String data = "LoanId=3001, MediaId=25, MemberId=16, Checkout=1000, Due=2000, Returned=3000\n";
		Files.write(new File(filename).toPath(), data.getBytes());
		
		loanRepo.loadLoansFromFile(filename);
		
		assertEquals(1, loanRepo.getHistory().size());
		
		Loan loan= loanRepo.getHistory().get(0);
		
		assertEquals(3001, loan.getLoanId());
		assertEquals(25, loan.getMediaId());
		assertEquals(16, loan.getMemberId());
		
		new File(filename).delete();
	}

}
