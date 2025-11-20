package LibraryGUI;

public class BoardGame {
    private int id;
    private String title;
    private String rating;
    private String playerCount; // e.g., "2-4"
    private int gameLengthMinutes;
    private int totalQuantity;

    public BoardGame(int id, String title, String rating, String playerCount, int gameLengthMinutes, int totalQuantity) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.playerCount = playerCount;
        this.gameLengthMinutes = gameLengthMinutes;
        this.totalQuantity = totalQuantity;
    }

    public int getId() { 
    	return id;
    }
    public String getTitle() { 
    	return title; 
    }
    public String getRating() {
    	return rating;
    }
    public String getPlayerCount() { 
    	return playerCount;
    }
    public int getGameLengthMinutes() { 
    	return gameLengthMinutes; 
    }
    public int getTotalQuantity() { 
    	return totalQuantity; 
    }

    public void setTitle(String title) { 
    	this.title = title; 
    }
    public void setRating(String rating) { 
    	this.rating = rating;
    }
    public void setPlayerCount(String playerCount) {
    	this.playerCount = playerCount; 
    }
    public void setGameLengthMinutes(int gameLengthMinutes) { 
    	this.gameLengthMinutes = gameLengthMinutes; 
    }
    public void setTotalQuantity(int q) { 
    	this.totalQuantity = q;
    }
}
