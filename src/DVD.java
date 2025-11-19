
public class DVD extends Media{
	private String title;
	private Rating ageRating;
	private int runTime;
	
	DVD (String newTitle, Rating newAgeRating, int newRunTime) {
		this.title = newTitle;
		this.ageRating =  newAgeRating;
		this.runTime = newRunTime;
	}
	
	private DVD () {
		this.title = null;
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
