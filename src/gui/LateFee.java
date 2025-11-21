package gui;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LateFee {
    public enum Status {
        PENDING, PAID, WAIVED
    }

    private int id;
    private int loanId;
    private int memberId;
    private MediaType mediaType;
    private int mediaId;
    private LocalDateTime assessedAt;
    private int daysLate;
    private int amountCents;
    private Status status;
    private LocalDateTime paidAt;
    private String waivedReason;

    public LateFee(int id, int loanId, int memberId, MediaType mediaType, int mediaId,
                   LocalDateTime assessedAt, int daysLate, int amountCents, Status status) {
        this.id = id;
        this.loanId = loanId;
        this.memberId = memberId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.assessedAt = assessedAt;
        this.daysLate = daysLate;
        this.amountCents = amountCents;
        this.status = (status == null ? Status.PENDING : status);
    }

    public LateFee(int id, int loanId, int memberId, int daysLate, int amountCents, LocalDate assessedOn) {
        this(id, loanId, memberId, null, 0,
             assessedOn != null ? assessedOn.atStartOfDay() : LocalDateTime.now(),
             daysLate, amountCents, Status.PENDING);
    }

    public int getId() { 
    	return id;
    }
    public int getLoanId() {
    	return loanId;
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
    public LocalDateTime getAssessedAt() {
    	return assessedAt; 
    }
    public int getDaysLate() { 
    	return daysLate; 
    }
    public int getAmountCents() { 
    	return amountCents; 
    }
    public Status getStatus() {
    	return status;
    }
    public LocalDateTime getPaidAt() { 
    	return paidAt; 
    }
    public String getWaivedReason() { 
    	return waivedReason; 
    }

    public void setStatus(Status status) { 
    	this.status = status;
    }
    public void setPaidAt(LocalDateTime paidAt) {
    	this.paidAt = paidAt; 
    }
    public void setWaivedReason(String waivedReason) { 
    	this.waivedReason = waivedReason; 
    }

    public void markPaid(LocalDate when) {
        this.status = Status.PAID;
        this.paidAt = (when != null ? when.atStartOfDay() : LocalDateTime.now());
        this.waivedReason = null;
    }

    public void waive(String reason) {
        this.status = Status.WAIVED;
        this.waivedReason = reason;
        this.paidAt = null;
    }

    public String getAmountFormatted() {
        int dollars = amountCents / 100;
        int cents = Math.abs(amountCents % 100);
        return String.format("$%d.%02d", dollars, cents);
    }

    public String amountAsCurrency() {
        return getAmountFormatted();
    }

    @Override
    public String toString() {
        return "LateFee{id=" + id +
                ", loanId=" + loanId +
                ", memberId=" + memberId +
                ", amount=" + getAmountFormatted() +
                ", status=" + status +
                "}";
    }
}
