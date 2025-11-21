package Account;
import java.util.Date;

public class Hold {
	private int mediaId;
    private int memberId;
    private Date datePlaced;
    private Date holdUntilDate;

    public Hold(int mediaId, int memberId, Date datePlaced,  Date holdUntilDate) {
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.datePlaced = datePlaced;
        this.holdUntilDate = holdUntilDate;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public Integer getMemberId() {
        return memberId;
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