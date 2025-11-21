package Inventory;

class DVD extends Media{
	private Rating ageRating;
	private int runTime;
	
	DVD (String newTitle, // From base class Media
		String newPublisher, 
		String newGenre, 
		int newTotalQuantity,
		int quantityAvailable, 
		Rating newAgeRating, // Unique to DVD subclass
		int newRunTime)
	{
		this.title = newTitle;
		this.publisher = newPublisher;
		this.ageRating =  newAgeRating;
		this.runTime = newRunTime;
	}
	
	DVD () {
		this.title = null;
		this.publisher = null;
		this.genre = null;
		this.totalQuantity = 0;
		this.quantityAvailable = 0;
		this.ageRating = Rating.Unrated;
		this.runTime = 0;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Rating getAgeRating() {
		return this.ageRating;
	}
	
	public int getRunTime() {
		return this.runTime;
	}
	
	public String toString() {
		return getTitle() + "," +
		getAgeRating() + "," +
		getRunTime();
	}
	
	private void setTitle(String newTitle) {
		this.title = newTitle;
		return;
	}
	
	private void setAgeRating(Rating newAgeRating) {
		this.ageRating = newAgeRating;
		return;
	}
	
	private void setRunTime(int newRunTime) {
		this.runTime = newRunTime;
		return;
	}
}
