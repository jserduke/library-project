package inventory;

import java.util.ArrayList;

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
	
	public ArrayList<Media>getMediaItems() {
		return this.mediaItems;
	}
	
	public String toString() {
		String inventoryString = "Media Count: " + getNumMedia() + "\n";
		for (Media item : mediaItems) {
			inventoryString += item.toString();
		}
		return inventoryString;
	}
	
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
}
