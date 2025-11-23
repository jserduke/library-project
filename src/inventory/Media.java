package inventory;
public abstract class Media {
	protected static int nextMediaId = 0;
	private final MediaType mediaType;
	private int id; // all fields inherited by Book/DVD/Board Game
	private String title;
	private String publisher;
	private String genre;
	private int totalQuantity;
	private int quantityAvailable;

	// protected constructors for subclass use to initialize variables unaccessible to subclasses
	protected Media(MediaType newMediaType, 
	// int newId, 
	String newTitle, 
	String newPublisher, 
	String newGenre,
	int newTotalQuantity,
	int newQuantityAvailable) {
		this.mediaType = newMediaType;
		this.id = nextMediaId++;
		this.title = newTitle;
		this.publisher = newPublisher;
		this.genre = newGenre;
		this.totalQuantity = newTotalQuantity;
		this.quantityAvailable = newQuantityAvailable;
	}
	
	protected Media(MediaType newMediaType) { // default constructor
		this.mediaType = newMediaType;
		this.id = 0;
		this.title = null;
		this.publisher = null;
		this.genre = null;
		this.totalQuantity = 0;
		this.quantityAvailable = 0;
	}
	
	// public getters for Media superclass variables
	public MediaType getMediaType() {
		return this.mediaType;
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
	
	// toString returns all applicable Media info, subclasses have extra info
	public String toString() {
		return  "Media Info{ID:" + this.id + 
		",Title:" + this.title + 
		",Publisher:" + this.publisher + 
		",Genre:" + this.genre + 
		",Total Quantity:" + this.totalQuantity + 
		",Quantity Available:" + this.quantityAvailable + "}\n";
	}
	
	// Setters are protected for subclass access since variables are private
	protected void setId(int newId) {
		this.id = newId;
	}
	
	protected void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	protected void setPublisher(String newPublisher) {
		this.publisher = newPublisher;
	}
	
	protected void setGenre(String newGenre) {
		this.genre = newGenre;
	}
	
	protected void setTotalQuantity(int newTotalQuantity) {
		this.totalQuantity = newTotalQuantity;
	}
	
	protected void setQuantityAvailable(int newQuantityAvailable) {
		this.quantityAvailable = newQuantityAvailable;
	}
	
	protected void setQuantityAll(int newTotalQuantity, int newQuantityAvailable) {
		setTotalQuantity(newTotalQuantity);
		setQuantityAvailable(newQuantityAvailable);
	}
}
