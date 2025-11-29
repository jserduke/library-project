package test.inventoryJUnitTest;

import inventory.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.*;

@DisplayName("Abstract Media Class Tests")
class mediaTest {
	Book book;
	DVD dvd;
	BoardGame boardGame;

	@BeforeEach
	void setUp() {
		book = new Book(1, "World War Z", "BookPublisher", "Sci-Fi", 5, 3, "Max Brooks", 12345.67, "978-1234567890");
		dvd = new DVD(2, "Dawn of the Dead", "DVDPublisher", "Horror", 10, 5, Rating.R, 101);
		boardGame = new BoardGame(3, "Catan", "BoardGamePublisher", "Strategy", 3, 2, Rating.G, 3, 6, 50);
	}
	
	@Nested
	@DisplayName("getMediaType() Tests")
	class GetMediaTypeTests {
		
		@DisplayName("getMediaType Book Test")
		@Test
		void getMediaTypeTestBook() {
			assertEquals(MediaType.BOOK, book.getMediaType());
		}
	
		@DisplayName("getMediaType DVD Test")
		@Test
		void getMediaTypeTestDVD() {
			assertEquals(MediaType.DVD, dvd.getMediaType());
		}
	
		@DisplayName("getMediaType BoardGame Test") 
		@Test
		void getMediaTypeTestBoardGame() {
			assertEquals(MediaType.BOARD_GAME, boardGame.getMediaType());
		}
	}
	
	@Nested
	@DisplayName("getId() Tests")
	class GetIdTests {
		
		@DisplayName("getId Book Test")
		@Test
		void getIdTestBook() {
			assertEquals(1, book.getId());
		}
	
		@DisplayName("getId DVD Test")
		@Test
		void getIdTestDVD() {
			assertEquals(2, dvd.getId());
		}
	
		@DisplayName("getId BoardGame Test")
		@Test
		void getIdTestBoardGame() {
			assertEquals(3, boardGame.getId());
		}
	}
	
	@Nested
	@DisplayName("getTitle() Tests")
	class GetTitleTests {
		
		@DisplayName("getTitle Book Test")
		@Test
		void getTitleTestBook() {
			assertEquals("World War Z", book.getTitle());
		}
		
		@DisplayName("getTitle DVD Test")
		@Test
		void getTitleTestDVD() {
			assertEquals("Dawn of the Dead", dvd.getTitle());
		}
		
		@DisplayName("getTitle BoardGame Test")
		@Test
		void getTitleTestBoardGame() {
			assertEquals("Catan", boardGame.getTitle());
		}
	}
	
	@Nested
	@DisplayName("getPublisher() Tests")
	class GetPublisherTests {
		
		@DisplayName("getPublisher Book Test")
		@Test
		void getPublisherTestBook() {
			assertEquals("BookPublisher", book.getPublisher());
		}
		
		@DisplayName("getPublisher DVD Test")
		@Test
		void getPublisherTestDVD() {
			assertEquals("DVDPublisher", dvd.getPublisher());
		}
		
		@DisplayName("getPublisher BoardGame Test")
		@Test
		void getPublisherTestBoardGame() {
			assertEquals("BoardGamePublisher", boardGame.getPublisher());
		}
	}
	
	@Nested
	@DisplayName("getGenre() Tests")
	class GetGenreTests {
		
		@DisplayName("getGenre Book Test")
		@Test
		void getGenreTestBook() {
			assertEquals("Sci-Fi", book.getGenre());
		}
		
		@DisplayName("getGenre DVD Test")
		@Test
		void getGenreTestDVD() {
			assertEquals("Horror", dvd.getGenre());
		}
		
		@DisplayName("getGenre BoardGame Test")
		@Test
		void getGenreBoardGame() {
			assertEquals("Strategy", boardGame.getGenre());
		}
	}
	
	@Nested
	@DisplayName("getTotalQuantity() Tests")
	class GetTotalQuantityTests {
		
		@DisplayName("getTotalQuantity Book Test")
		@Test
		void getTotalQuantityTestBook() {
			assertEquals(5, book.getTotalQuantity());
		}
		
		@DisplayName("getTotalQuantity DVD Test")
		@Test
		void getTotalQuantityTestDVD() {
			assertEquals(10, dvd.getTotalQuantity());
		}
		
		@DisplayName("getTotalQuantity BoardGame Test")
		@Test
		void getTotalQuantityTestBoardGame() {
			assertEquals(3, boardGame.getTotalQuantity());
		}
	}
	
	@Nested
	@DisplayName("getQuantityAvailable() Tests")
	class GetQuantityAvailableTests {
		
		@DisplayName("getQuantityAvailable Book Test")
		@Test
		void getQuantityAvailableTestBook() {
			assertEquals(3, book.getQuantityAvailable());
		}
		
		@DisplayName("getQuantityAvailable DVD Test")
		@Test
		void getQuantityAvailableTestDVD() {
			assertEquals(5, dvd.getQuantityAvailable());
		}
		
		@DisplayName("getQuantityAvailable BoardGame Test")
		@Test
		void getQuantityAvailableTestBoardGame() {
			assertEquals(2, boardGame.getQuantityAvailable());
		}
	}
	
	@Nested
	@DisplayName("toString() Tests")
	class ToStringTests {
		
		@DisplayName("toString Book Test")
		@Test
		void toStringTestBook() {
			String expectedBookString = "Media Info{ID:1;Title:World War Z;Publisher:BookPublisher;Genre:Sci-Fi;Total Quantity:5;Quantity Available:3}BOOK:[Author:Max Brooks;DDNumber:12345.67;ISBN:978-1234567890]\n";
			assertEquals(expectedBookString, book.toString());
		}
		
		@DisplayName("toString DVD Test")
		@Test
		void toStringTestDVD() {
			String expectedDVDString = "Media Info{ID:2;Title:Dawn of the Dead;Publisher:DVDPublisher;Genre:Horror;Total Quantity:10;Quantity Available:5}DVD:[AgeRating:R;RunTime:101]\n";
			assertEquals(expectedDVDString, dvd.toString());
		}
		
		@DisplayName("toString BoardGame Test")
		@Test
		void toStringTestBoardGame() {
			String expectedBoardGameString = "Media Info{ID:3;Title:Catan;Publisher:BoardGamePublisher;Genre:Strategy;Total Quantity:3;Quantity Available:2}BOARD_GAME:[AgeRating:G;Min Players:3;Max Players:6;Game Length:50]\n";
			assertEquals(expectedBoardGameString, boardGame.toString());
		}
	}
	
	@Nested
	@DisplayName("setId() Tests")
	class SetIdTests {
		
		@DisplayName("setId Book Test")
		@Test
		void setIdTestBook() {
			book.setId(10);
			assertEquals(10, book.getId());
		}
		
		@DisplayName("setId DVD Test")
		@Test
		void setIdTestDVD() {
			dvd.setId(20);
			assertEquals(20, dvd.getId());
		}
		
		@DisplayName("setId BoardGame Test")
		@Test
		void setIdTestBoardGame() {
			boardGame.setId(30);
			assertEquals(30, boardGame.getId());
		}
	}
	
	
	@Nested
	@DisplayName("setTitle() Tests")
	class SetTitleTests {
		
		@DisplayName("setTitle Book Test")
		@Test
		void setTitleTestBook() {
			book.setTitle("Hatchet");
			assertEquals("Hatchet", book.getTitle());
		}
		
		@DisplayName("setTitle DVD Test")
		@Test
		void setTitleTestDVD() {
			dvd.setTitle("Train to Busan");
			assertEquals("Train to Busan", dvd.getTitle());
		}
		
		@DisplayName("setTitle BoardGame Test")
		@Test
		void setTitleTestBoardGame() {
			boardGame.setTitle("Monopoly");
			assertEquals("Monopoly", boardGame.getTitle());
		}
	}
	
	@Nested
	@DisplayName("setPublisher() Tests")
	class SetPublisherTests {
		
		@DisplayName("setPublisher Book Test")
		@Test
		void setPublisherTestBook() {
			book.setPublisher("NewBookPublisher");
			assertEquals("NewBookPublisher", book.getPublisher());
		}
		
		@DisplayName("setPublisher DVD Test")
		@Test
		void setPublisherTestDVD() {
			dvd.setPublisher("NewDVDPublisher");
			assertEquals("NewDVDPublisher", dvd.getPublisher());
		}
		
		@DisplayName("setPublisher BoardGame Test")
		@Test
		void setPublisherTestBoardGame() {
			boardGame.setPublisher("NewBoardGamePublisher");
			assertEquals("NewBoardGamePublisher", boardGame.getPublisher());
		}
	}
	
	@Nested
	@DisplayName("setGenre() Tests")
	class SetGenreTests {
		
		@DisplayName("setGenre Book Test")
		@Test
		void setGenreTestBook() {
			book.setGenre("Autobiography");
			assertEquals("Autobiography", book.getGenre());
		}
		
		@DisplayName("setGenre DVD Test")
		@Test
		void setGenreTestDVD() {
			dvd.setGenre("Action");
			assertEquals("Action", dvd.getGenre());
		}
		
		@DisplayName("setGenre BoardGame Test")
		@Test
		void setGenreTestBoardGame() {
			boardGame.setGenre("Family");
			assertEquals("Family", boardGame.getGenre());
		}
	}
	
	@Nested
	@DisplayName("setTotalQuantity() Tests")
	class SetTotalQuantityTests {
		
		@DisplayName("setTotalQuantity Book Test")
		@Test
		void setTotalQuantityTestBook() {
			book.setTotalQuantity(7);
			assertEquals(7, book.getTotalQuantity());
		}
		
		@DisplayName("setTotalQuantity DVD Test")
		@Test
		void setTotalQuantityTestDVD() {
			dvd.setTotalQuantity(15);
			assertEquals(15, dvd.getTotalQuantity());
		}
		
		@DisplayName("setTotalQuantity BoardGame Test")
		@Test
		void setTotalQuantityTestBoardGame() {
			boardGame.setTotalQuantity(4);
			assertEquals(4, boardGame.getTotalQuantity());
		}
	}
	
	@Nested
	@DisplayName("setQuantityAvailable() Tests")
	class SetQuantityAvailableTests {
		
		@DisplayName("setQuantityAvailable Book Test")
		@Test
		void setQuantityAvailableTestBook() {
			book.setQuantityAvailable(1);
			assertEquals(1, book.getQuantityAvailable());
		}
		
		@DisplayName("setQuantityAvailable DVD Test")
		@Test
		void setQuantityAvailableTestDVD() {
			dvd.setQuantityAvailable(2);
			assertEquals(2, dvd.getQuantityAvailable());
		}
		
		@DisplayName("setQuantityAvailable BoardGame Test")
		@Test
		void setQuantityAvailableTestBoardGame() {
			boardGame.setQuantityAvailable(3);
			assertEquals(3, boardGame.getQuantityAvailable());
		}
	}
	
	@Nested
	@DisplayName("setQuantityAll() Tests")
	class SetQuantityAllTests {
		
		@DisplayName("setQuantityAll Book Test")
		@Test
		void setQuantityAllTestBook() {
			book.setQuantityAll(8, 4);
			assertAll(
				() -> assertEquals(8, book.getTotalQuantity()),
				() -> assertEquals(4, book.getQuantityAvailable())
			);
		}
		
		@DisplayName("setQuantityAll DVD Test")
		@Test
		void setQuantityAllTestDVD() {
			dvd.setQuantityAll(20, 10);
			assertAll(
				() -> assertEquals(20, dvd.getTotalQuantity()),
				() -> assertEquals(10, dvd.getQuantityAvailable())
			);
		}
		
		@DisplayName("setQuantityAll BoardGame Test")
		@Test
		void setQuantityAllTestBoardGame() {
			boardGame.setQuantityAll(5, 2);
			assertAll("Total and Available Quantity Check",
				() -> assertEquals(5, boardGame.getTotalQuantity()),
				() -> assertEquals(2, boardGame.getQuantityAvailable())
			);
		}
	}
	
	@Nested
	@DisplayName("increaseAvailable(int plus) Tests")
	class IncreaseAvailableTests {
		
		@DisplayName("increaseAvailable Book Test")
		@Test
		void increaseAvailableTestBook() {
			book.increaseAvailable(2);
			assertEquals(5, book.getQuantityAvailable());
		}
		
		@DisplayName("increaseAvailable DVD Test")
		@Test
		void increaseAvailableTestDVD() {
			dvd.increaseAvailable(3);
			assertEquals(8, dvd.getQuantityAvailable());
		}
		
		@DisplayName("increaseAvailable BoardGame Test")
		@Test
		void increaseAvailableTestBoardGame() {
			boardGame.increaseAvailable(1);
			assertEquals(3, boardGame.getQuantityAvailable());
		}
	}
}
