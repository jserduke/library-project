package inventory;

abstract class Media {
	protected int id; // all fields inherited by Book/DVD/Board Game
	protected String title;
	protected String publisher;
	protected String genre;
	protected int totalQuantity;
	protected int quantityAvailable;

protected Media(int newId, 
	String newTitle, 
	String newPublisher, 
	String newGenre,
	int newTotalQuantity,
	int newQuantityAvailable) {
		this.id = newId;
		this.title = newTitle;
		this.publisher = newPublisher;
		this.genre = newGenre;
		this.totalQuantity = newTotalQuantity;
		this.quantityAvailable = newQuantityAvailable;
	}
	
	public Media() {
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
	
	public int getQuantityAvailable() {
		return this.quantityAvailable;
	}
	
	private void setId(int newId) {
		this.id = newId;
	}
	
	private void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	private void setPublisher(String newPublisher) {
		this.publisher = newPublisher;
	}
	
	private void setGenre(String newGenre) {
		this.genre = newGenre;
	}
	
	private void setTotalQuantity(int newTotalQuantity) {
		this.totalQuantity = newTotalQuantity;
	}
	
	private void setQuantityAvailable(int newQuantityAvailable) {
		this.quantityAvailable = newQuantityAvailable;
	}
	
	private void setQuantityAll(int newTotalQuantity, int newQuantityAvailable) {
		setTotalQuantity(newTotalQuantity);
		setQuantityAvailable(newQuantityAvailable);
	}
	
	public abstract String toString();
}
