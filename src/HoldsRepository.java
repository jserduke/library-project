import java.io.*;
import java.util.Date;
import java.util.ArrayList;

public class HoldsRepository {
	private ArrayList<Hold> holds;
	private Integer numHolds;
	
	public HoldsRepository() {
		this.holds = new ArrayList<>();
		this.numHolds = 0;
	}
	
	public ArrayList<Hold> getHolds() {
		return holds;
	}
	
	public Hold placeHold(Integer mediaId, Integer memberId, Date until) {
		return null;
	}
	
	public Boolean cancelHold(Integer mediaId, Integer memberId) {
		return false;
	}
	
	public Integer cancelExpiredHolds() {
		int removedCount = 0;
		Date now = new Date();
		
		
		return removedCount;
	}
}