import java.util.ArrayList;

public class Inventory {
	private int numMedia;
	private ArrayList<Media> mediaItems;
	
	
	Inventory(int newNumMedia, ArrayList<Media> newMediaItems) {
		this.numMedia = newNumMedia;
		this.mediaItems = newMediaItems;
	}
	
	Inventory(ArrayList<Media> newMediaItems) {
		this.mediaItems = newMediaItems;
		this.numMedia = newMediaItems.size();
	}
	
	public int getNumMedia() {
		return this.numMedia;
	}
	
	public ArrayList<Media>getMediaItems() {
		return this.mediaItems;
	}
	
	
}
