import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import inventory.*;

@DisplayName("Inventory Package Test")
class inventoryTest {

	ArrayList<Media> mediaList;
	Inventory inventory;
	Book book1 = new Book(1, "BookTitle", "BookPublisher", "BookGenre", 1, 1, "BookAuthor", 123, "1234");
	DVD dvd1 = new DVD(2, "DvdTitle", "DvdPublisher", "DvdGenre", 1, 1, Rating.PG_13, 60);
	BoardGame boardgame1 = new BoardGame(3, "BoardGameTitle", "BoardGamePublisher", "BoardGameGenre", 1, 1, Rating.G, 2, 4, 30);
	
	@BeforeEach
	void setUp() {
		mediaList = new ArrayList<Media>();
		inventory = new Inventory(mediaList);
	}
	
	@Nested
	@DisplayName("Inventory Getters Tests")
	class InventoryGettersTests {
		
		@DisplayName("getNumMedia() Empty ArrayList Test")
		@Test
		void getNumMediaEmptyTest() {
			assertEquals(0, inventory.getNumMedia());
		}
		
		@DisplayName("getNumMedia() Populated ArrayList Test")
		@Test
		void getNumMediaPopulatedTest() {
			inventory.add(book1);
			inventory.add(dvd1);
			inventory.add(boardgame1);
			assertEquals(3, inventory.getNumMedia());
		}
		
		@DisplayName("getMediaItems() Empty ArrayList Test")
		@Test
		void getMediaItemsEmpty() {
			assertNotNull(inventory.getMediaItems());
		}
		
		@DisplayName("getMediaItems() Populated ArrayList Test")
		@Test
		void getMediaItemsPopulatedTest() {
			inventory.add(book1);
			inventory.add(dvd1);
			inventory.add(boardgame1);
			ArrayList<Media> items = inventory.getMediaItems();
			assertAll("MediaItems Contents Test",
				() -> assertEquals(3, items.size()),
				() -> assertTrue(items.contains(book1)),
				() -> assertTrue(items.contains(dvd1)),
				() -> assertTrue(items.contains(boardgame1))
			);
		}
	}

	@DisplayName("toString() Empty Test")
	@Test
	void toStringEmptyTest() {
		String expected = "Media Count: 0\n";
		assertEquals(expected, inventory.toString());
	}
	
	@DisplayName("toString() Populated Test")
	@Test
	void toStringPopulatedTest() {
		mediaList.add(book1);
		mediaList.add(dvd1);
		mediaList.add(boardgame1);
		String expected = 
			"Media Count: 1\n" + book1.toString() +
			"Media Count: 2\n" + dvd1.toString() +
			"Media Count: 3\n" + boardgame1.toString();
		assertEquals(expected, inventory.toString());
	}
	
	@DisplayName("addMedia() Empty Test")
	@Test
	void addMediaEmptyTest() {
		inventory.addMedia(book1);
		ArrayList<Media> expectedItems = new ArrayList<Media>();
		expectedItems.add(book1);
		assertEquals(expectedItems, inventory.getMediaItems());
	}
	
	@DisplayName("addMedia() Populated Test")
	@Test
	void addMediaPopulatedTest() {
		inventory.addMedia(book1);
		inventory.addMedia(dvd1);
		ArrayList<Media> expectedItems = new ArrayList<Media>();
		expectedItems.add(book1);
		expectedItems.add(dvd1);
		assertEquals(expectedItems, inventory.getMediaItems());
	}
	
	@DisplayName("searchByTitle() Found Test")
	@Test
	void searchByTitleFoundTest() {
		inventory.addMedia(book1);
		inventory.addMedia(dvd1);
		Media result = inventory.searchByTitle("DvdTitle");
		assertEquals(dvd1, result);
	}
	
	@DisplayName("searchByTitle() Not Found Test")
	@Test
	void searchByTitleNotFoundTest() {
		inventory.addMedia(book1);
		inventory.addMedia(dvd1);
		Media result = inventory.searchByTitle("NonExistentTitle");
		assertNull(result);
	}
	
	@DisplayName("searchByISBN() Found Test")
	@Test
	void searchByISBNFoundTest() {
		inventory.addMedia(book1);
		Media result = inventory.searchByISBN("1234");
		assertEquals(book1, result);
	}
	
	@DisplayName("searchByISBN() Not Found Test")
	@Test
	void searchByISBNNotFoundTest() {
		inventory.addMedia(book1);
		Media result = inventory.searchByISBN("9999");
		assertNull(result);
	}
	
	@DisplayName("searchByID() Found Test")
	@Test
	void searchByIDFoundTest() {
		inventory.addMedia(dvd1);
		Media result = inventory.searchByID(2);
		assertEquals(dvd1, result);
	}
	
	@DisplayName("searchByID() Not Found")
	@Test
	void searchByIDNotFoundTest() {
		inventory.addMedia(dvd1);
		Media result = inventory.searchByID(999);
		assertNull(result);
	}
	
	@DisplayName ("ifNameEmptyDefaultOutPut Empty String Test")
	@Test
	void ifNameEmptyDefaultOutPutTest() {
		String expected = "inventory_output";
		assertEquals(expected, inventory.ifNameEmptyDefaultOutPut(""));
	}
	
	@DisplayName("ifNameEmptyDefaultOutPut Non-Empty String Test")
	@Test
	void ifNameEmptyDefaultOutPutNonEmptyTest() {
		String expected = "custom_name";
		assertEquals(expected, inventory.ifNameEmptyDefaultOutPut("custom_name"));
	}
}
