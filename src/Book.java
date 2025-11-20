
public class Book extends Media{
	private String title;
	private String author;
	private double ddCallNumber;
	private String isbn;
	private String publisher;
	
	private Book(String newTitle, String newAuthor, double newDdCallNumber, String newIsbn, String newPublisher) {
		this.title = newTitle;
		this.author = newAuthor;
		this.ddCallNumber = newDdCallNumber;
		this.isbn = newIsbn;
		this.publisher = newPublisher;
	}
	
	private Book() {
		this.title = null;
		this.author = null;
		this.ddCallNumber = 0;
		this.isbn = null;
		this.publisher = null;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public double getDdCallNumber() {
		return this.ddCallNumber;
	}
	
	public String getIsbn() {
		return this.isbn;
	}
	
	public String getPublisher() {
		return this.publisher;
	}
	
	public String toString() {
		return "Book {" + 
		"Title:" + getTitle() + 
		", Author:" + getAuthor() + 
		", DD Number:" + getDdCallNumber() + 
		", ISBN:" + getIsbn() + 
		", Publisher:" + getPublisher() + "}\n";
	}
	
	private void setTitle(String newTitle) {
		this.title = newTitle;
		return;
	}
	
	private void setAuthor(String newAuthor) {
		this.author = newAuthor;
		return;
	}
	
	private void setDdCallNumber(double newDdCallNumber) {
		this.ddCallNumber = newDdCallNumber;
		return;
	}
	
	private void setIsbn(String newIsbn) {
		this.isbn = newIsbn;
		return;
	}
	
	private void setPublisher(String newPublisher) {
		this.publisher = newPublisher;
		return;
	}
}
