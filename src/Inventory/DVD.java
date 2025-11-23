package Inventory;

class DVD extends Media{
	private Rating ageRating;
	private int runTime;
	
	protected DVD (int newId, // For Media super() variables
		String newTitle,
		String newPublisher,
		String newGenre, 
		int newTotalQuantity,
		int quantityAvailable,
		Rating newAgeRating, // Variables unique to DVD subclass
		int newRunTime)
	{
		super(MediaType.DVD,newId, newTitle, newPublisher, newGenre, newTotalQuantity, quantityAvailable);
		this.ageRating =  newAgeRating;
		this.runTime = newRunTime;
	}
	
	protected DVD () { // default constructor
		super(MediaType.DVD);
		this.ageRating = Rating.UNRATED;
		this.runTime = 0;
	}
	
	// public getters for DVD subclass variables
	public String getAgeRating() {
		return ageRating.name();
	}
	
	public int getRunTime() {
		return this.runTime;
	}
	
	@Override
	public String toString() {
		return super.toString() +
		getMediaType() + 
		" Category Info:{" + 
		ageRating.name() + ":" + getAgeRating() + 
		",RunTime:" + getRunTime() + "}\n";
	}
	
	// private setters for DVD subclass variables
	private void setAgeRating(Rating newAgeRating) {
		this.ageRating = newAgeRating;
		return;
	}
	
	private void setRunTime(int newRunTime) {
		this.runTime = newRunTime;
		return;
	}
}
