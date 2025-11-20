package LibraryGUI;

public class Dvd {
    private int id;
    private String title;
    private String rating;
    private int runtimeMinutes;
    private int totalQuantity;
    private String studio;

    public Dvd(int id, String title, String rating, int runtimeMinutes, int totalQuantity) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
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
    public int getRuntimeMinutes() { 
    	return runtimeMinutes; 
    }
    public int getTotalQuantity() { 
    	return totalQuantity;
    }
    public String getStudio(){ 
    	return studio; 
    }
    
    public void setTitle(String title) { 
    	this.title = title;
    }
    public void setRating(String rating) { 
    	this.rating = rating;
    }
    public void setRuntimeMinutes(int runtimeMinutes) { 
    	this.runtimeMinutes = runtimeMinutes;
    }
    public void setTotalQuantity(int q) { 
    	this.totalQuantity = q; 
    }
    public Dvd studio(String s){ 
    	this.studio = s; 
    	return this; 
    }
}
