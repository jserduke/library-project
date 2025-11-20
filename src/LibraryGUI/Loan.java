package LibraryGUI;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int memberId;
    private MediaType mediaType;
    private int mediaId;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int gracePeriodDays;

    public Loan(int id, int memberId, MediaType mediaType, int mediaId,
                LocalDate checkoutDate, LocalDate dueDate, int gracePeriodDays) {
        this.id = id;
        this.memberId = memberId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.gracePeriodDays = gracePeriodDays;
    }

    public int getId() { 
    	return id; 
    }
    public int getMemberId() { 
    	return memberId; 
    }
    public MediaType getMediaType() { 
    	return mediaType; 
    }
    public int getMediaId() {
    	return mediaId; 
    }
    public LocalDate getCheckoutDate() {
    	return checkoutDate; 
    }
    public LocalDate getDueDate() {
    	return dueDate; 
    }
    public LocalDate getReturnDate() {
    	return returnDate; 
    }
    public int getGracePeriodDays() {
    	return gracePeriodDays; 
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    // Late fee helpers 

    public int computeDaysLate(LocalDate now) {
        LocalDate end = (getReturnDate() != null) ? getReturnDate() : now;
        LocalDate lateStart = getDueDate().plusDays(getGracePeriodDays());
        
        if (!end.isAfter(lateStart)) 
        	return 0;
        long days = java.time.temporal.ChronoUnit.DAYS.between(lateStart, end);
        return (int) Math.max(1, days);
    }


    public int computeLateFeeCents(int perDayCents, int capCents, LocalDate now) {
        int daysLate = computeDaysLate(now);
        
        if (daysLate <= 0) 
        	return 0;
        long raw = (long) daysLate * (long) perDayCents;
        return (int) Math.min((long) capCents, raw);
    }
}
