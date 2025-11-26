package account;
import java.util.Date;

public class Loan {
	private int loanId;
	private int mediaId;
	private int memberId;
    private Date dateCheckedOut;
    private Date dateDue;
    private Date dateReturned;

    public Loan(int loanId, int mediaId, int memberId, Date checkoutDate, Date dueDate) {
    	this.loanId = loanId;
    	this.mediaId = mediaId;
        this.memberId = memberId;
        this.dateCheckedOut = checkoutDate;
        this.dateDue = dueDate;
        this.dateReturned = null;
        
    }
    
    public int getLoanId() {
    	return loanId;
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
        		"\nLoan Id: " + loanId +
                "Media Id: " + mediaId +
                "\nDate checked out: " + dateCheckedOut +
                "\n Date due: " + dateDue +
                "\n Return date: " + dateReturned;
    }
}