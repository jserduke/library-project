package gui;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private int totalQuantity;

    public Book(int id, String isbn, String title, String author, String publisher, String genre, int totalQuantity) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.totalQuantity = totalQuantity;
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
    public String getPublisher() { 
    	return publisher; 
    }
    public String getGenre() { 
    	return genre;
    }
    public int getTotalQuantity() { 
    	return totalQuantity; 
    }

    public void setTitle(String title) { 
    	this.title = title; 
    }
    public void setAuthor(String author) {
    	this.author = author; 
    }
    public void setPublisher(String publisher) {
    	this.publisher = publisher; 
    }
    public void setGenre(String genre) { 
    	this.genre = genre; 
    }
    public void setTotalQuantity(int q) { 
    	this.totalQuantity = q;
    }
}
