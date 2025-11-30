package account;

import java.util.Date;

public class Hold {
	private int holdId;
	private int mediaId;
    private int memberId;
    private int holdLimit;
    private Date datePlaced;
    private Date holdUntilDate;
    private HoldStatus holdStatus;

    public Hold(int holdId, int mediaId, int memberId, Date datePlaced,  Date holdUntilDate, HoldStatus holdStatus) {
    	this.holdId = holdId;
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.datePlaced = datePlaced;
        this.holdUntilDate = holdUntilDate;
        this.holdStatus = holdStatus;
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
        return holdStatus;
    }

    public void setStatus(HoldStatus holdStatus) {
        this.holdStatus = holdStatus;
    }
  
    public String toString() {
        return "Hold: " +
                "mediaId= " + mediaId + ", memberId= " + memberId + ", holdUntilDate= " + holdUntilDate + '}';
    }
}