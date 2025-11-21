package message;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
	private static int count  = 0;
	private int id;
	private Date datetime;
	private int libraryId;
	private Type type;
	private int requestId;
	private Action action;
	private Status status;
	private ArrayList<String> info;
	
	public Message(int libraryId, Type type, int requestId, Action action, Status status, ArrayList<String> info) {
		id = count++;
		datetime = new Date();
		this.libraryId = libraryId;
		this.type = type;
		this.requestId = requestId;
		this.action = action;
		this.status = status;
		this.info = info;
	}
	
	public static int getCount() {
		return count;
	}
	
	public int getId() {
		return id;
	}
	public Date getDatetime() {
		return datetime;
	}
	public int getLibraryId() {
		return libraryId;
	}
	public Type getType() {
		return type;
	}
	public int getRequestId() {
		return requestId;
	}
	public Status getStatus() {
		return status;
	}
	public Action getAction() {
		return action;
	}
	public ArrayList<String> getInfo() {
		return info;
	}
	
	public String toString() {
		return "id: " + id + " (datetime: " + datetime + ")\nlibrary id: " + libraryId + " | type: " + type
			 + " | request id: " + requestId + " | status: " + status + " | action: " + action + "\ninfo: " + info;
	}
}
