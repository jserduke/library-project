package LibraryGUI;

public class Book {
	private int id;
	private String isbn;
	private String title;
	private String author;
	private String genre;
	private int quantity;
	private boolean available;

	public Book(int id, String isbn, String title,
             	String author, String genre, int quantity, boolean available) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.quantity = quantity;
		this.available = available;
	}

	public int getId() {
		return id; 
	}
	public String getIsbn() {
		return isbn; 
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() { 
		return author;
	}
	public String getGenre() { 
		return genre; 
	}
	public int getQuantity() { 
		return quantity; 
	}
	public boolean isAvailable() { 
		return available; 
	}

	public void setTitle(String title) {
		this.title = title; 
	}
	public void setAuthor(String author) { 
		this.author = author;
	}
	public void setGenre(String genre) { 
		this.genre = genre;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setAvailable(boolean available) { 
		this.available = available; 
	}

}

