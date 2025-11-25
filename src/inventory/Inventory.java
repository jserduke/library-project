package inventory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
	private int numMedia;
	private ArrayList<Media> mediaItems; // array of Media superclass, accepts subclass objects
	
	public Inventory(int newNumMedia, ArrayList<Media> newMediaItems) {
		this.numMedia = newNumMedia;
		this.mediaItems = newMediaItems;
	}
	
	public Inventory(ArrayList<Media> newMediaItems) {
		this.mediaItems = newMediaItems;
		this.numMedia = newMediaItems.size();
	}
	
	public int getNumMedia() {
		return this.numMedia;
	}
	
	public ArrayList<Media> getMediaItems() {
		return this.mediaItems;
	}
	
	public String toString() {
		String inventoryString = "Media Count: " + getNumMedia() + "\n";
		for (Media item : mediaItems) {
			inventoryString += item.toString();
		}
		return inventoryString;
	}
	
	// switch back to private?
	public void addMedia(Media newMedia) {
		switch(newMedia.getMediaType()) {
			case MediaType.BOOK:
				this.mediaItems.add(newMedia);
				break;
			case MediaType.DVD:
				this.mediaItems.add(newMedia);
				break;
			case MediaType.BOARD_GAME:
				this.mediaItems.add(newMedia);
				break;
			default:
				System.out.println("Error: Invalid Media Type - " + newMedia.getMediaType());
				return;
		}
		numMedia++;
		System.out.println("Media Item Added: " + newMedia.toString() + " at position " + (numMedia-1) + "\n");
		return;
	} // end of addMedia() method
	
	public ArrayList<Media> searchByTitle(String title) {
		ArrayList<Media> foundMediaList = new ArrayList<Media>();
		for (Media item : mediaItems) {
			if (item.getTitle().equalsIgnoreCase(title)) {
				foundMediaList.add(item);
			}
		}
		if (foundMediaList.isEmpty()) {
			System.out.println("No results found with title: " + title + "\n");
			return null;
		}
		else {
			System.out.println("Search results with title: " + title + "\n");
			return foundMediaList;
		}
	} // end of searchByTitle() method
	
	public ArrayList<Media> searchByISBN(String isbn) {
		ArrayList<Media> foundMediaList = new ArrayList<Media>();
		for (Media item : mediaItems) {
			if (item.getMediaType() == MediaType.BOOK) {
				Book bookItem = (Book) item; // explicitly downcast Media to Book to access getIsbn() method
				if (bookItem.getIsbn().equalsIgnoreCase(isbn)) {
					foundMediaList.add(bookItem);
				}
			}
		}
		if (foundMediaList.isEmpty()) {
			System.out.println("No results found with ISBN: " + isbn + "\n");
			return null;
		}
		else {
			System.out.println("Search results with ISBN: " + isbn + "\n");
			return foundMediaList;
		}
	} // end of searchByISBN() method
	
	public ArrayList<Media> searchByID(int id) {
		ArrayList<Media> foundMediaList = new ArrayList<Media>();
		for (Media item : mediaItems) {
			if (item.getId() == id) {
				foundMediaList.add(item);
			}
		}
		if (foundMediaList.isEmpty()) {
			System.out.println("No results found with ID: " + id + "\n");
			return null;
		}
		else {
			System.out.println("Search results with ID: " + id + "\n");
			return foundMediaList;
		}
	} // end of searchByID() method
	
	public void saveInventoryToFile(String filename) {
		ifNameEmptyDefaultOutput(filename);
		File file = new File(filename + ".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Media item : mediaItems) {
				writer.write("Item #" + (mediaItems.indexOf(item) + 1) + ":");
				writer.write(item.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		if (file.isFile()) {
			System.out.println("Inventory saved to file:" + file.getAbsolutePath() + "\n");
		} else {
			System.out.println("Error: Inventory file not saved.\n");
		}
	}
	
	public void ifNameEmptyDefaultOutput(String filename) {
		if (filename == null || filename.isEmpty()) {
			filename = "inventory_output";
		}
	}
	
	// right now, read from file located in src/inventory/fileoutput
	public void loadInventoryFromFile(String filename) {
		Path filePath = Paths.get("src", "inventory", "fileoutput", filename + ".txt");
		System.out.println("Looking at: " + filePath.toAbsolutePath()+ "\n");
		if (!Files.isRegularFile(filePath)) {
			System.out.println("Error: Inventory file not found:" + filePath.toAbsolutePath() + "\n");
			return;
		}
		File file = filePath.toFile();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("Read line: " + line); // For testing purposes
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Media parseMedia(String line) {
		line = line.trim();
		int closingBraceIndex = line.indexOf('}');
		if (closingBraceIndex == -1) {
			throw new IllegalArgumentException("Invalid media string format: " + line);
		}
		String basePart = line.substring(0, closingBraceIndex + 1);
		String extraPart = line.substring(closingBraceIndex + 1).trim();
		
		Map<String, String> baseFields = parseBaseFields(basePart);
		
		// Extract base Media fields
		int id = Integer.parseInt(baseFields.get("ID"));
		String title = baseFields.get("Title");
		String publisher = baseFields.get("Publisher");
		String genre = baseFields.get("Genre");
		int totalQuantity = Integer.parseInt(baseFields.get("Total Quantity"));
		int quantityAvailable = Integer.parseInt(baseFields.get("Quantity Available"));
		
		/* Media type + detail section
		 * extraPart looks like:
		 * "BOOK:[Author:John Doe;DDNumber:123.45;ISBN:9783161484100]"
		 * "BOARD_GAME:[AgeRating:PG-13;Min Players:2;Max Players:4;Game Length:60]"
		 * DVD:[AgeRating:PG;RunTime:120]
		*/
		if (extraPart.isEmpty()) {
			throw new IllegalArgumentException("Missing media type information: " + line);
		}
		int typeSepIndex = extraPart.indexOf(":[");
		if (typeSepIndex == -1) {
			throw new IllegalArgumentException("Invalid media type format: " + extraPart);
		}
		String mediaTypeString = extraPart.substring(0, typeSepIndex).trim();	// e.g. "BOOK"
		String detailPart = extraPart.substring(typeSepIndex + 2);				// after ":["
		int endBracketIndex = detailPart.lastIndexOf(']');
		if (endBracketIndex != -1) {
			detailPart = detailPart.substring(0, endBracketIndex);
		}
		detailPart = detailPart.trim();
		
		MediaType mediaType = MediaType.valueOf(mediaTypeString); // uses enum names BOOK, DVD, BOARD_GAME
		Media media; // valid because object not actually created, just a reference
		
		switch (mediaType) {
			case BOOK:
				media = parseBookDetails(detailPart, title, publisher, genre, totalQuantity, quantityAvailable);
				break;
			case DVD:
				media = parseDVDDetails(detailPart, title, publisher, genre, totalQuantity, quantityAvailable);
				break;
			case BOARD_GAME:
				media = parseBoardGameDetails(detailPart, title, publisher, genre, totalQuantity, quantityAvailable);
				break;
			default:
				throw new IllegalArgumentException("Unknown media type: " + mediaTypeString);
		}
		return media;
	}
	
	private Media parseBookDetails(
			String detailPart,
			String title,
			String publisher,
			String genre,
			int totalQuantity,
			int quantityAvailable) {
				return null;
	}
	
	private Media parseBoardGameDetails(String detailPart, String title, String publisher, String genre,
			int totalQuantity, int quantityAvailable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Media parseDVDDetails(String detailPart, String title, String publisher, String genre, int totalQuantity,
			int quantityAvailable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Map<String, String> parseBaseFields(String wholeString) {
		int braceStart = wholeString.indexOf("{");
		int braceEnd = wholeString.indexOf("}");
		if (braceStart == -1 || braceEnd == -1 || braceEnd <= braceStart) {
			throw new IllegalArgumentException("Invalid media string format: " + wholeString);
		}
		// Extract Media substring between braces
		String innerMediaString = wholeString.substring(braceStart + 1, braceEnd);
		Map<String, String> fields = new HashMap<>();
		
		// Split by semicolon 
		// ie. Media Info{ID:1;Title:Catan;Publisher:Kosmos;Genre:Strategy;Total Quantity:5;Quantity Available:3}
		// => ID:1, Title:Catan, Publisher:Kosmos,Genre: Strategy,TotalQuantity:5,QuantityAvailable:3
		String[] parts = innerMediaString.split(";");
		for (String part : parts) {
			// Remove leading/trailing whitespace
			part = part.trim();
			if (part.isEmpty()) {
				continue; // skip empty parts
			}
			// Split on first ':' to separate key and value
			String[] keyValue = part.split(":", 2);
			if (keyValue.length != 2) {
				throw new IllegalArgumentException("Invalid key-value pair: " + part);
			}
			String key = keyValue[0].trim(); // e.g. "ID"
			String value = keyValue[1].trim(); // e.g. "1"
			fields.put(key, value);
		}
		return fields;
		/* end of parseMediaAttributes()
		SHOULD give us
		fields.get("ID")
		fields.get("Title")
		fields.get("Publisher")
		fields.get("Genre")
		fields.get("Total Quantity")
		fields.get("Quantity Available")
		*/
	}
}
