package Inventory;

public class BoardGame extends Media{ // Variables unique to BoardGame subclass
	private Rating ageRating;
	private int playerCountMin;
	private int playerCountMax;
	private int gameLength;
	
	 // inherited from base class Media, used in super() parent constructor call
	protected BoardGame(int newId,
		String newTitle,
		String newPublisher,
		String newGenre,
		int newTotalQuantity,
		int newQuantityAvailable,
		Rating newAgeRating, // unique to Boardgame subclass, used for subclass constructor call
		int newPlayerCountMin,
		int newPlayerCountMax,
		int newGameLength) 
	{
		super(MediaType.BOARD_GAME,newId,newTitle,newPublisher,newGenre,newTotalQuantity,newQuantityAvailable);
		this.ageRating = newAgeRating; // regular constructor for BoardGame subclass
		this.playerCountMin = newPlayerCountMin;
		this.playerCountMax = newPlayerCountMax;
		this.gameLength = newGameLength;
	}
	
	protected BoardGame() {
		super(MediaType.BOARD_GAME); // calls Media superclass default constructor
		this.ageRating = Rating.UNRATED;
		this.playerCountMin = 0;
		this.playerCountMax = 0;
		this.gameLength = 0;
	}
	
	// public getters for BoardGame subclass variables
	public Rating getRating() {
		return ageRating;
	}
	
	public int getPlayerCountMin() {
		return playerCountMin;
	}
	
	public int getPlayerCountMax() {
		return playerCountMax;
	}
	
	public int getGameLength() {
		return gameLength;
	}
	
	@Override
	public String toString() {
		return super.toString() +
		getMediaType() + 
		" Category Info:{" + 
		ageRating.name() + ":" + getRating() + 
		",Min Players:" + getPlayerCountMin() + 
		",Max Players:" + getPlayerCountMax() + 
		",Game Length:" + getGameLength() + "}\n";
	}
	
	private void setRating(Rating newRating) {
		this.ageRating = newRating;
		return;
	}
	
	private void setPlayerCountMin(int newPlayerCountMin) {
		this.playerCountMin = newPlayerCountMin;
		return;
	}
	
	private void setPlayerCountMax(int newPlayerCountMax) {
		this.playerCountMax = newPlayerCountMax;
		return;
	}
	
	private void setGameLength(int newGameLength) {
		this.gameLength = newGameLength;
		return;
	}
}
