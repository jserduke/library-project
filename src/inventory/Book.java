package inventory;

public class Book extends Media{
	private String author;
	private double ddCallNumber;
	private String isbn;
	
	private Book(int newId, // inherited from base class Media
		String newTitle,
		String newPublisher,
		String newGenre,
		int newTotalQuantity,
		int newQuantityAvailable,
		String newAuthor,	// unique to Book subclass
		double newDdCallNumber, 
		String newIsbn) {
			this.id = newId;
			this.title = newTitle;
			this.publisher = newPublisher;
			this.genre = newGenre;
			this.totalQuantity = newTotalQuantity;
			this.quantityAvailable = newQuantityAvailable;
			this.author = newAuthor;
			this.ddCallNumber = newDdCallNumber;
			this.isbn = newIsbn;
	}
	
	private Book() {
		this.id = 0;	// inherited from base class Media
		this.title = null;
		this.publisher = null;
		this.genre = null;
		this.totalQuantity = 0;
		this.quantityAvailable = 0;
		this.author = null;	// unique to Book subclass
		this.ddCallNumber = 0;
		this.isbn = null;
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

	// return String of Media info separated by commas within curly braces
	// w/ newline separating Book info separated by commas within curly braces
	public String toString() {
		return "Media Info{" +
		"ID:" + getId() +
		",Publisher:" + getPublisher() +
		",Genre:" + getGenre() +
		",Total Quantity:" + getTotalQuantity() +
		",Quantity Available:" + getQuantityAvailable() + "\n" +
		"Book Info{" +
		"Author:" + getAuthor() +
		",DD Number:" + getDdCallNumber() +
		",ISBN:" + getIsbn() + "}\n";
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
