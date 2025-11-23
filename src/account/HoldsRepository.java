package account;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class HoldsRepository {
	private ArrayList<Hold> holds;
	private int numHolds;
	
	public HoldsRepository() {
		this.holds = new ArrayList<>();
		this.numHolds = 0;
	}
	
	public ArrayList<Hold> getHolds() {
		return holds;
	}
	
	public Hold placeHold(Integer mediaId, Integer memberId, Date until) {
		for (Hold h : holds) {
			if (h.getMediaId() == mediaId && h.getMemberId() == memberId) {
				return null;
			}
		}
		
		Hold newHold = new Hold(mediaId, memberId, new Date(), until);
		holds.add(newHold);
		numHolds++;
		return newHold;
	}
	
	public Boolean cancelHold(Integer mediaId, Integer memberId) {
		for (int i = 0; i < holds.size(); i++) {
			Hold hold = holds.get(i);
			if (hold.getMediaId() == mediaId && hold.getMediaId() == memberId) {
				holds.remove(i);
				numHolds--;
				return true;
			}
		}
		return false;
	}
	
	public int cancelExpiredHolds() {
		int removedCount = 0;
		Date now = new Date();
		
		for (int i = 0; i < holds.size(); i++) {
			Hold hold = holds.get(i);
			if (hold.getHoldUntilDate().before(now)) {
				holds.remove(i);
				i--;
				numHolds--;
				removedCount++;
			}
		}
		
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
		try {
			File file = new File(filename);
			
			if (!file.exists()) {
				System.out.println(filename + " not found. No holds loaded.");
				return;
			}
		
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				
				String[] str = line.split(", ");
				if (str.length != 4) {
					continue;
				}
				
				int mediaId = Integer.parseInt(str[0]);
				int memberId = Integer.parseInt(str[1]);
				long placedMillis = Long.parseLong(str[2]);
				long untilMillis = Long.parseLong(str[3]);
				
				boolean alreadyExists = false;
				for (Hold old : holds) {
					if (old.getMediaId() == mediaId &&
						old.getMemberId() == memberId &&
						old.getDatePlaced().getTime() == placedMillis) {
						alreadyExists = true;
						break;
					}
				}
				
				if (alreadyExists) {
					continue;
				}
				
				Hold hold = new Hold(mediaId, memberId, new Date(placedMillis), new Date(untilMillis));
				
				holds.add(hold);
				numHolds++;
			}
		} catch (Exception e) {
			System.out.println("Error reading: " + filename + ": " + e.getMessage());
		}
	}
	
}