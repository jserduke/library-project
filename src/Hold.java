import java.util.Date;

public class Hold {
	private int mediaId;
    private int memberId;
    private Date holdUntilDate;

    public Hold(int mediaId, int memberId, Date until) {
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.holdUntilDate = until;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Date getHoldUntilDate() {
        return holdUntilDate;
    }
  
    public String toString() {
        return "Hold{" +
                "mediaId= " + mediaId + ", memberId= " + memberId + ", holdUntilDate= " + holdUntilDate + '}';
    }
}