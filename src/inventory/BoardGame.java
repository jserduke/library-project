package inventory;

public class BoardGame extends Media{ // Variables unique to BoardGame subclass
	private Rating ageRating;
	private int playerCountMin;
	private int playerCountMax;
	private int gameLength;
	
	 // inherited from base class Media, used in super() parent constructor call
	public BoardGame(int newId,
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
	
	public BoardGame() {
		super(MediaType.BOARD_GAME); // calls Media superclass default constructor
		this.ageRating = Rating.UNRATED;
		this.playerCountMin = 0;
		this.playerCountMax = 0;
		this.gameLength = 0;
	}
	
	public MediaType getMediaType() {
		return MediaType.BOARD_GAME;
	}
	
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
	
	public void setRating(Rating newRating) {
		this.ageRating = newRating;
		return;
	}
	
	public void setPlayerCountMin(int newPlayerCountMin) {
		this.playerCountMin = newPlayerCountMin;
		return;
	}
	
	public void setPlayerCountMax(int newPlayerCountMax) {
		this.playerCountMax = newPlayerCountMax;
		return;
	}
	
	public void setGameLength(int newGameLength) {
		this.gameLength = newGameLength;
		return;
	}
}
