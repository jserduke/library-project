import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class HoldsRepository {
	private ArrayList<Hold> holds;
	private Integer numHolds;
	
	public HoldsRepository() {
		this.holds = new ArrayList<>();
		this.numHolds = 0;
	}
	
	public ArrayList<Hold> getHolds() {
		return holds;
	}
	
	public Hold placeHold(Integer mediaId, Integer memberId, Date until) {
		return null;
	}
	
	public Boolean cancelHold(Integer mediaId, Integer memberId) {
		return false;
	}
	
	public Integer cancelExpiredHolds() {
		int removedCount = 0;
		Date now = new Date();
		
		
		return removedCount;
	}
	
	public void saveHoldToFile(String filename) {
		File file = new File(filename);
		
		try {
			if (file.createNewFile()) {
				System.out.println(filename + " created!");
			}
			
			else {
				System.out.println(filename + " already exists. Overwriting...");
			}
			
			FileWriter writer = new FileWriter(file);
			
			for (Hold h : holds) {
				writer.write(h.getMediaId() + ", " +
							 h.getMemberId() + ", " +
							 h.getHoldUntilDate().getTime() + "\n");
			}
			
			writer.close();
			System.out.println("Holds have to " + filename);
		} catch (IOException e) {
			System.out.println("Error writing to " + filename + ": " + e.getMessage());
		}
	}
	
	public void loanHoldsFromFile(String filename) {
		File file = new File(filename);
		
		if (!file.exists()) {
			System.out.println(filename + " not found. No holds loaded.");
			return;
		}
		
		System.out.println("Loading holds from " + filename + "...");
		
		holds.clear();
		numHolds = 0;
		
		try {
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				//needs more code
			}
		} catch (Exception e) {
			System.out.println("Error reading " + filename + ": " + e.getMessage());
		}
	}
	
}