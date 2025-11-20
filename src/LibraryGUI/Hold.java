package LibraryGUI;

import java.time.LocalDate;

public class Hold {
    private int id;
    private int memberId;
    private MediaType mediaType;
    private int mediaId;
    private LocalDate holdUntil;

    public Hold(int id, int memberId, MediaType mediaType, int mediaId, LocalDate holdUntil) {
        this.id = id;
        this.memberId = memberId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.holdUntil = holdUntil;
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
    public LocalDate getHoldUntil() { 
    	return holdUntil;
    }
}
