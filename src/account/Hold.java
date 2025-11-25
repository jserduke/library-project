package account;
import java.util.Date;

public class Hold {
	private int holdId;
	private int mediaId;
    private int memberId;
    private int holdLimit;
    private Date datePlaced;
    private Date holdUntilDate;

    public Hold(int mediaId, int memberId, Date datePlaced,  Date holdUntilDate) {
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.datePlaced = datePlaced;
        this.holdUntilDate = holdUntilDate;
    }
    
    public int getHoldId() {
    	return holdId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getMemberId() {
        return memberId;
    }
    
    public int getHoldLimit() {
    	return holdLimit;
    }
    
    public Date getDatePlaced() {
    	return datePlaced;
    }

    public Date getHoldUntilDate() {
        return holdUntilDate;
    }
  
    public String toString() {
        return "Hold: " +
                "mediaId= " + mediaId + ", memberId= " + memberId + ", holdUntilDate= " + holdUntilDate + '}';
    }
}