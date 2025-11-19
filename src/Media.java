
public class Media {
	private int id;
	private String title;
	private String publisher;
	private String genre;
	private int totalQuantity;
	private int quantityAvailable;
	
	private Media(int newId, 
			String newTitle, 
			String newPublisher, 
			String newGenre,
			int newTotalQuantity,
			int newQuantityAvailable)
	{
		this.id = newId;
		this.title = newTitle;
		this.publisher = newPublisher;
		this.genre = newGenre;
		this.totalQuantity = newTotalQuantity;
		this.quantityAvailable = newQuantityAvailable;
	}
	
	private Media() {
		this.id = 0;
		this.title = null;
		this.publisher = null;
		this.genre = null;
		this.totalQuantity = 0;
		this.quantityAvailable = 0;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getPublisher() {
		return this.publisher;
	}
	
	public String getGenre() {
		return this.genre;
	}
	
	public int getTotalQuantity() {
		return this.totalQuantity;
	}
}
