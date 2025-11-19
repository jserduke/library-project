import java.util.Date;

public class Loan {

    private Integer mediaId;
    private Date dateCheckedOut;
    private Date dateDue;
    private Date dateReturned;
    private Date gracePeriod;

    public Loan(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.dateCheckedOut = checkoutDate;
    }

    public void setReturnDate(Date returnDate) {
        this.dateReturned = returnDate;
    }

    public void setDateDue(Date dueDate) {
        this.dateDue = dueDate;
    }

    public void setGracePeriod(Date gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    
    public String toString() {
        return "Loan{" +
                "mediaId=" + mediaId +
                ", dateCheckedOut=" + dateCheckedOut +
                ", dateDue=" + dateDue +
                ", dateReturned=" + dateReturned +
                ", gracePeriod=" + gracePeriod +
                '}';
    }
}
