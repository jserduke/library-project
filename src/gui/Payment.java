package gui;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int memberId;
    private int amountCents;
    private String method;
    private String reference;
    private LocalDateTime receivedAt;

    public Payment(int id, int memberId, int amountCents, String method, String reference, LocalDateTime receivedAt) {
        this.id = id;
        this.memberId = memberId;
        this.amountCents = amountCents;
        this.method = method;
        this.reference = reference;
        this.receivedAt = receivedAt;
    }

    public int getId() {
    	return id;
    }
    public int getMemberId() {
    	return memberId; 
    }
    public int getAmountCents() { 
    	return amountCents;
    }
    public String getMethod() {
    	return method;
    }
    public String getReference() {
    	return reference;
    }
    public LocalDateTime getReceivedAt() {
    	return receivedAt; 
    }
}
