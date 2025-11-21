package Account;
import java.util.Date;

public class Loan {
	private int mediaId;
	private int memberId;
    private Date dateCheckedOut;
    private Date dateDue;
    private Date dateReturned;

    public Loan(int mediaId, int memberId, Date checkoutDate, Date dueDate) {
        this.mediaId = mediaId;
        this.memberId = memberId;
        this.dateCheckedOut = checkoutDate;
        this.dateDue = dueDate;
        this.dateReturned = null;
    }

    public int getMediaId() {
        return mediaId;
    }
    
    public int getMemberId() {
        return memberId;
    }
    
    public Date getCheckoutDate() {
        return dateCheckedOut;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.dateCheckedOut = checkoutDate;
    }
    
    public Date getReturnedDate() {
        return dateReturned;
    }

    public void setReturnDate(Date returnDate) {
        this.dateReturned = returnDate;
    }

    public Date getDueDate() {
        return dateDue;
    }
    
    public void setDateDue(Date dueDate) {
        this.dateDue = dueDate;
    }
    
    public String toString() {
        return "Loan:\n" +
                "Media Id: " + mediaId +
                "\nMember Id: " + memberId +
                "\nDate Checkedout: " + dateCheckedOut +
                "\n Date due: " + dateDue +
                "\n Return date: " + dateReturned;
    }
}