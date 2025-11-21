package Inventory;

public class BoardGame extends Media{
	private String title;
	private Rating ageRating;
	private int playerCountMin;
	private int playerCountMax;
	private int gameLength;
	
	private BoardGame(String newTitle, Rating newAgeRating, int newPlayerCountMin, int newPlayerCountMax, int newGameLength) {
		this.title = newTitle;
		this.ageRating = newAgeRating;
		this.playerCountMin = newPlayerCountMin;
		this.playerCountMax = newPlayerCountMax;
		this.gameLength = newGameLength;
	}
	
	private BoardGame() {
		this.title = null;
		this.ageRating = Rating.Unrated;
		this.playerCountMin = 0;
		this.playerCountMax = 0;
		this.gameLength = 0;
	}
	
	public String getTitle() {
		return this.title;
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
	
	public String toString() {
		return "Board Game {" +
	"Title:" + getTitle() + 
	", Rating:" + getRating() + 
	", Min Players:" + getPlayerCountMin() + 
	", Max Players:" + getPlayerCountMax() + 
	", Game Length:" + getGameLength() + "}\n";
	}
	
	private void setTitle(String newTitle) {
		this.title = newTitle;
		return;
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
}
