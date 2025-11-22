package Inventory;

public class Book extends Media{
	private String author;
	private double ddCallNumber;
	private String isbn;
	
	protected Book(int newId, // variables inherited from base class Media
		String newTitle,
		String newPublisher,
		String newGenre,
		int newTotalQuantity,
		int newQuantityAvailable,
		String newAuthor,	// variables unique to Book subclass
		double newDdCallNumber, 
		String newIsbn) 
	{
		super(MediaType.BOOK,newId, newTitle, newPublisher, newGenre, newTotalQuantity, newQuantityAvailable);
		this.author = newAuthor; //
		this.ddCallNumber = newDdCallNumber;
		this.isbn = newIsbn;
	}
	
	protected Book() {
		super(MediaType.BOOK); // calls Media superclass default constructor
		this.author = null;
		this.ddCallNumber = 0;
		this.isbn = null;
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

	@Override
	public String toString() {
		return super.toString() +
		getMediaType() + 
		" Category Info:{Author:" + getAuthor() +
		",DD Number:" + getDdCallNumber() +
		",ISBN:" + getIsbn() + "}\n";
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
}
