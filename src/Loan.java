import java.util.Date;

public class Loan {
	private int mediaId;
	private int memberId;
    private Date dateCheckedOut;
    private Date dateDue;
    private Date dateReturned;
    private Date gracePeriod;

    public Loan(int mediaId, int memberId, Date checkoutDate, Date dueDate) {
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.dateCheckedOut = checkoutDate;
        this.dateDue = dueDate;
        this.gracePeriod = null;
    }

    public int getMediaId() {
        return mediaId;
    }
    
    public int getMemberId() {
        return memberId;
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
        return "Loan\n" +
                "Media Id: " + mediaId +
                "\nDate Checkedout: " + dateCheckedOut +
                "\n Date due: " + dateDue +
                "\n Return date: " + dateReturned +
                "\n Grace period: " + gracePeriod ;
    }
}