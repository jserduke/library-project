package inventoryTest;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import inventory.*;

@DisplayName("Inventory Class Test")
public class inventoryTest {

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
	@DisplayName("Inventory Getter() Tests")
	class InventoryGettersTests {
		
		@DisplayName("getNumMedia() Empty ArrayList Test")
		@Test
		void getNumMediaEmptyTest() {
			assertEquals(0, inventory.getNumMedia());
		}
		
		@DisplayName("getNumMedia() Populated ArrayList Test")
		@Test
		void getNumMediaPopulatedTest() {
			mediaList.add(book1);
			mediaList.add(dvd1);
			mediaList.add(boardgame1);
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
			mediaList.add(book1);
			mediaList.add(dvd1);
			mediaList.add(boardgame1);
			ArrayList<Media> items = inventory.getMediaItems();
			assertAll("MediaItems Contents Test",
				() -> assertEquals(3, items.size()),
				() -> assertTrue(items.contains(book1)),
				() -> assertTrue(items.contains(dvd1)),
				() -> assertTrue(items.contains(boardgame1))
			);
		}
	}

	@Nested
	@DisplayName("Inventory toString() Tests")
	class InventoryToStringTests {
		
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
			"Media Count: 3\n" + book1.toString() +
			dvd1.toString() + 
			boardgame1.toString();
			assertEquals(expected, inventory.toString());
		}
	}
	
	@Nested
	@DisplayName("Inventory addMedia() Tests")
	class InventoryAddMediaTests {
		
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
	}
	
	@Nested
	@DisplayName("Inventory SearchBy() Tests")
	class InventorySearchTests {
		
		@Nested
		@DisplayName("searchByTitle() Tests")
		class InventorySearchByTitleTests {
			@DisplayName("searchByTitle() Found Test")
			@Test
			void searchByTitleFoundTest() {
				inventory.addMedia(book1);
				inventory.addMedia(dvd1);
				ArrayList<Media> result = inventory.searchByTitle("DvdTitle");
				ArrayList<Media> expectedResult = new ArrayList<Media>();
				expectedResult.add(dvd1);
				assertEquals(result, expectedResult);
			}
	
			@DisplayName("searchByTitle() Not Found Test")
			@Test
			void searchByTitleNotFoundTest() {
				inventory.addMedia(book1);
				inventory.addMedia(dvd1);
				ArrayList<Media> result = inventory.searchByTitle("NonExistentTitle");
				assertNull(result);
			}
		}
		
		@Nested
		@DisplayName("searchByISBN() Tests")
		class InventorySearchByISBNTests {
			
			@DisplayName("searchByISBN() Found Test")
			@Test
			void searchByISBNFoundTest() {
				inventory.addMedia(book1);
				ArrayList<Media> result = inventory.searchByISBN("1234");
				ArrayList<Media> expectedResult = new ArrayList<Media>();
				expectedResult.add(book1);
				assertEquals(result, expectedResult);
			}
	
			@DisplayName("searchByISBN() Not Found Test")
			@Test
			void searchByISBNNotFoundTest() {
				inventory.addMedia(book1);
				ArrayList<Media> result = inventory.searchByISBN("9999");
				assertNull(result);
			}
		}
		
		@Nested
		@DisplayName("searchByID() Tests")
		class InventorySearchByIDTests {
			
			@DisplayName("searchByID() Found Test")
			@Test
			void searchByIDFoundTest() {
				inventory.addMedia(dvd1);
				ArrayList<Media> result = inventory.searchByID(2);
				ArrayList<Media> expectedResult = new ArrayList<Media>();
				expectedResult.add(dvd1);
				assertEquals(result, expectedResult);
			}
	
			@DisplayName("searchByID() Not Found")
			@Test
			void searchByIDNotFoundTest() {
				inventory.addMedia(dvd1);
				ArrayList<Media> result = inventory.searchByID(999);
				assertNull(result);
			}
		}
	}
	
	@Nested
	@DisplayName("ifNameEmptyDefaultOutput() Tests")
	class ifNameEmptyDefaultOutputTests {
	
		@DisplayName ("ifNameEmptyDefaultOutPut Empty String Test")
		@Test
		void ifNameEmptyDefaultOutPutTest() {
			String expected = "inventory_output";
			assertEquals(expected, inventory.ifNameEmptyDefaultOutput(""));
		}

		@DisplayName("ifNameEmptyDefaultOutPut Non-Empty String Test")
		@Test
		void ifNameEmptyDefaultOutPutNonEmptyTest() {
			String expected = "custom_name";
			assertEquals(expected, inventory.ifNameEmptyDefaultOutput("custom_name"));
		}
	}
	
	/*
	 * File I/O hard :(
	@Nested
	@DisplayName("saveInventory() & loadInventory() Tests")
	class SaveLoadInventoryTests {
		
		@TempDir
		Path tempDir;
		
		@BeforeEach
		void setUpFileIO() throws IOException{
			System.setProperty("user.dir", tempDir.toString());
			Files.createDirectories(tempDir.resolve("resources").resolve("output"));
			Files.createDirectories(tempDir.resolve("resources").resolve("input"));
		}
		
		@DisplayName("saveInventory() Default Output Test")
		@Test
		void saveInventoryToFileTest() throws IOException {
			inventory.addMedia(book1);
			inventory.addMedia(dvd1);
			inventory.addMedia(boardgame1);
			inventory.saveInventoryToFile("");
			Path outputFile = tempDir.resolve(Paths.get("resources", "output", "inventory_output.txt"));
			String content = Files.readString(outputFile);
			String expectedContent = 
				"Item #1:" + book1.toString() +
				"Item #2:" + dvd1.toString() +
				"Item #3:" + boardgame1.toString();
			assertAll("File DefaultName Existence & Content Test",
				() -> assertTrue(Files.exists(outputFile)),
				() -> assertEquals(content, expectedContent)
			);
		}
		
		@DisplayName("saveInventory() Custom Name Test")
		@Test
		void saveInventoryToFileCustomNameTest() throws IOException {
			inventory.addMedia(book1);
			inventory.addMedia(dvd1);
			inventory.addMedia(boardgame1);
			inventory.saveInventoryToFile("custom_name");
			Path outputFile = tempDir.resolve(Paths.get("resources", "output", "custom_name.txt"));
			String content = Files.readString(outputFile);
			String expectedContent = 
				"Item #1:" + book1.toString() +
				"Item #2:" + dvd1.toString() +
				"Item #3:" + boardgame1.toString();
			assertAll("File CustomName Existence & Content Test",
				() -> assertTrue(Files.exists(outputFile)),
				() -> assertEquals(content, expectedContent)
			);
		}
		
		@DisplayName("loadInventory() Invalid File Test")
		@Test
		void loadInventoryInvalidFileTest() {
			// starting from empty inventory (or capture size before)
			assertTrue(inventory.getMediaItems().isEmpty());
			inventory.loadInventoryFromFile("non_existent_file");
			assertTrue(inventory.getMediaItems().isEmpty(),
					"Inventory should remain empty if file does not exist");
		}

		
		@DisplayName("loadInventory() Valid File Test")
		@Test
		void loadInventoryValidFileTest() throws IOException {
			mediaList.add(book1);
			mediaList.add(dvd1);
			mediaList.add(boardgame1);
			Inventory expectedInventory = new Inventory(mediaList);
			
			// create resources/input directory inside tempDir
			Path inputFile = tempDir.resolve(Paths.get("resources", "input", "test_inventory.txt"));
			Files.createDirectories(inputFile.getParent());
			try (var writer = Files.newBufferedWriter(inputFile)) {
				int index = 1;
				for (Media item: expectedInventory.getMediaItems()) {
					writer.write("Item #" + index + ":" + item.toString());
					index++;
				}
			}
			inventory.loadInventoryFromFile("junit_test_inventory");
			assertEquals(expectedInventory.getNumMedia(), inventory.getNumMedia()),
			for (int i = 0; i < expectedInventory.getNumMedia(); i++) {
				assertEquals(
						expectedInventory.getMediaItems().get(i).toString(), 
						inventory.getMediaItems().get(i).toString()
							);
			}
		}
	}
	*/
	
	@Nested
	@DisplayName("parse Media Types() Tests")
	class ParseMediaTests {
		
		@Nested
		@DisplayName("parseMedia() Book Tests")
		class ParseMediaTypeTests {

			@DisplayName("parseMedia() parses valid BOOK Line Test")
			@Test
			void parseMediaValidBookLineTest() {
				String bookLine = 
						"Item#1:" +
						"Media Info{ID:1;Title:BookTitle;Publisher:BookPublisher;Genre:BookGenre;Total Quantity:1;Quantity Available:1}" +
						"BOOK:[Author:BookAuthor;DDNumber:123.00;ISBN:1234]\n";
				Media parsedMedia = inventory.parseMedia(bookLine);
				
				assertNotNull(parsedMedia, "Parsed media should not be null for valid book line");
				assertTrue(parsedMedia instanceof Book, "Parsed media should be an instance of Book");
				Book book = (Book) parsedMedia;
				
				assertAll("Parsed Book Media Test",
						() -> assertEquals(1, book.getId()),
						() -> assertEquals("BookTitle", book.getTitle()),
						() -> assertEquals("BookPublisher", book.getPublisher()),
						() -> assertEquals("BookGenre", book.getGenre()),
						() -> assertEquals(1, book.getTotalQuantity()),
						() -> assertEquals(1, book.getQuantityAvailable()),
						() -> assertEquals("BookAuthor", book.getAuthor()),
						() -> assertEquals(123.00, book.getDdCallNumber()),
						() -> assertEquals("1234", book.getIsbn())
						);
			}
			
			@DisplayName("parseMedia() parses valid DVD line")
			@Test
			void parseMediaValidDVDLineTest() {
				String dvdLine =
					"Item#2:"+
					"Media Info{ID:2;Title:DvdTitle;Publisher:DvdPublisher;Genre:DvdGenre;Total Quantity:1;Quantity Available:1}" +
					"DVD:[AgeRating:PG_13;RunTime:60]\n";

				Media parsedMedia = inventory.parseMedia(dvdLine);

				assertNotNull(parsedMedia, "Parsed media should not be null for valid DVD line");
				assertTrue(parsedMedia instanceof DVD, "Parsed media should be an instance of DVD");
				DVD dvd = (DVD) parsedMedia;

				assertAll("Parsed DVD Media Test",
					() -> assertEquals(2, dvd.getId()),
					() -> assertEquals("DvdTitle", dvd.getTitle()),
					() -> assertEquals("DvdPublisher", dvd.getPublisher()),
					() -> assertEquals("DvdGenre", dvd.getGenre()),
					() -> assertEquals(1, dvd.getTotalQuantity()),
					() -> assertEquals(1, dvd.getQuantityAvailable()),
					() -> assertEquals("PG_13", dvd.getAgeRating()),
					() -> assertEquals(60, dvd.getRunTime())
					);
			}
			
			@DisplayName("parseMedia() parses valid BOARD_GAME line")
			@Test
			void parseMediaValidBoardGameLineTest() {
				String boardGameLine =
					"Item#3:" + 
					"Media Info{ID:3;Title:BoardGameTitle;Publisher:BoardGamePublisher;Genre:BoardGameGenre;Total Quantity:1;Quantity Available:1}" +
					"BOARD_GAME:[AgeRating:G;Min Players:2;Max Players:4;Game Length:30]\n";

				Media parsedMedia = inventory.parseMedia(boardGameLine);

				assertNotNull(parsedMedia, "Parsed media should not be null for valid board game line");
				assertTrue(parsedMedia instanceof BoardGame, "Parsed media should be an instance of BoardGame");
				BoardGame game = (BoardGame) parsedMedia;

				assertAll("Parsed BoardGame Media Test",
						() -> assertEquals(3, game.getId()),
						() -> assertEquals("BoardGameTitle", game.getTitle()),
						() -> assertEquals("BoardGamePublisher", game.getPublisher()),
						() -> assertEquals("BoardGameGenre", game.getGenre()),
						() -> assertEquals(1, game.getTotalQuantity()),
						() -> assertEquals(1, game.getQuantityAvailable()),
						() -> assertEquals(Rating.G, game.getRating()),
						() -> assertEquals(2, game.getPlayerCountMin()),
						() -> assertEquals(4, game.getPlayerCountMax()),
						() -> assertEquals(30, game.getGameLength())
				);
			}
		}
		
		@Nested
		@DisplayName("parseMedia() Invalid Format Tests")
		class ParseMediaInvalidFormatTests {
			
			@Test
			@DisplayName("parseMedia() throws when '}' is missing in base part")
			void parseMedia_missingClosingBrace_throws() {
			// No '}' -> closingBraceIndex = -1
				String badLine =
					"Item #1:" + 
					"Media Info{ID:1;Title:Bad;Publisher:Bad;Genre:Bad;Total Quantity:1;Quantity Available:1" +
					"BOOK:[Author:X;DDNumber:1.0;ISBN:111]";
				assertThrows(IllegalArgumentException.class,
						() -> inventory.parseMedia(badLine));
			}
		
			@Test
			@DisplayName("parseMedia() throws when media type enum name is invalid")
			void parseMedia_invalidMediaTypeName_throws() {
				// "MAGAZINE" is not a valid MediaType enum constant
				String badLine =
						"Item #1:Media Info{ID:1;Title:BadEnum;Publisher:P;Genre:G;Total Quantity:1;Quantity Available:1}" +
						"MAGAZINE:[SomeField:Value]";

				assertThrows(IllegalArgumentException.class,
					() -> inventory.parseMedia(badLine));
			}
			
			@Test
			@DisplayName("parseMedia() throws when media type format is invalid (missing :[)")
			void parseMedia_invalidMediaTypeFormat_throws() {
				// extraPart doesn't contain ":["
				String badLine =
						"Item #1:Media Info{ID:1;Title:BadType;Publisher:P;Genre:G;Total Quantity:1;Quantity Available:1}BOOK[Author:X]";

				assertThrows(IllegalArgumentException.class,
						() -> inventory.parseMedia(badLine));
			}
			
			@DisplayName("parseMedia() throws when DVD age rating is invalid")
			@Test
			void parseMedia_invalidDVDAgeRating_throws() {
			    String badLine =
			        "Item#2:Media Info{ID:2;Title:BadDvd;Publisher:P;Genre:G;Total Quantity:1;Quantity Available:1}" +
			        "DVD:[AgeRating:NOT_A_RATING;RunTime:60]";

			    assertThrows(IllegalArgumentException.class,
			                 () -> inventory.parseMedia(badLine));
			}
			
			@DisplayName("parseMedia() propagates NumberFormatException from BOARD_GAME details")
			@Test
			void parseMedia_invalidBoardGamePlayerCount_throwsNumberFormatException() {
				String badLine =
				"Item#3:" +
				"Media Info{ID:3;Title:BadBoardGame;Publisher:P;Genre:G;Total Quantity:1;Quantity Available:1}" +
				"BOARD_GAME:[AgeRating:G;Min Players:two;Max Players:4;Game Length:30]";

				assertThrows(NumberFormatException.class,
						() -> inventory.parseMedia(badLine));
			}
		}
	}
	
}
