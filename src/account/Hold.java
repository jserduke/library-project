package account;
import java.util.Date;

//import gui.Hold.HoldStatus;

public class Hold {
	private int holdId;
	private int mediaId;
    private int memberId;
    private int holdLimit;
    private Date datePlaced;
    private Date holdUntilDate;
    private HoldStatus status;

    public Hold(int holdId, int mediaId, int memberId, Date datePlaced,  Date holdUntilDate, HoldStatus status) {
    	this.holdId = holdId;
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.datePlaced = datePlaced;
        this.holdUntilDate = holdUntilDate;
        this.status = status;
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
    
    public HoldStatus getStatus() {
        return status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }
  
    public String toString() {
        return "Hold: " +
                "mediaId= " + mediaId + ", memberId= " + memberId + ", holdUntilDate= " + holdUntilDate + '}';
    }
}