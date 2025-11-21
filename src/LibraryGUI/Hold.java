package LibraryGUI;

import java.time.LocalDate;

public class Hold {

    public enum HoldStatus { 
    	REQUESTED, 
    	ACTIVE, 
    	EXPIRED, 
    	CANCELLED, 
    	FULFILLED 
    }

    private int id;
    private int memberId;
    private MediaType mediaType;
    private int mediaId;

    private HoldStatus status;
    private LocalDate placedAt;
    private LocalDate activatedAt;
    private LocalDate holdUntil;
    private Integer queuePosition;
    private LocalDate pickupDeadline;


    public Hold(int id, int memberId, MediaType mediaType, int mediaId, LocalDate holdUntil) {
        this(
            id,
            memberId,
            mediaType,
            mediaId,
            HoldStatus.ACTIVE,
            LocalDate.now(),
            LocalDate.now(),
            holdUntil,
            null,
            holdUntil
        );
    }


    public Hold(int id,
                int memberId,
                MediaType mediaType,
                int mediaId,
                HoldStatus status,
                LocalDate placedAt,
                LocalDate activatedAt,
                LocalDate holdUntil,
                Integer queuePosition,
                LocalDate pickupDeadline) {
        this.id = id;
        this.memberId = memberId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.status = (status == null ? HoldStatus.REQUESTED : status);
        this.placedAt = (placedAt != null ? placedAt : LocalDate.now());
        this.activatedAt = activatedAt;
        this.holdUntil = holdUntil;
        this.queuePosition = queuePosition;
        this.pickupDeadline = pickupDeadline;
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

    public HoldStatus getStatus() {
        return status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public LocalDate getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDate placedAt) {
        this.placedAt = placedAt;
    }

    public LocalDate getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDate activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDate getHoldUntil() {
        return holdUntil;
    }

    public void setHoldUntil(LocalDate holdUntil) {
        this.holdUntil = holdUntil;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public LocalDate getPickupDeadline() {
        return pickupDeadline;
    }

    public void setPickupDeadline(LocalDate pickupDeadline) {
        this.pickupDeadline = pickupDeadline;
    }


    public boolean isExpired(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        LocalDate limit = (pickupDeadline != null ? pickupDeadline : holdUntil);
        return limit != null && today.isAfter(limit);
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        LocalDate limit = (pickupDeadline != null ? pickupDeadline : holdUntil);
        return status == HoldStatus.ACTIVE && limit != null && !today.isAfter(limit);
    }
}
