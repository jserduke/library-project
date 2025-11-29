package account;

import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class HoldsRepository {
	private int numHolds;
	private int nextHoldId = 2001;
	private ArrayList<Hold> holds;
	
	public HoldsRepository() {
		this.holds = new ArrayList<>();
		this.numHolds = 0;
	}
	
	public ArrayList<Hold> getHolds() {
		return holds;
	}
	
	public int getNumHolds() {
		return numHolds;
	}
	
	public Hold placeHold(int mediaId, int memberId, Date until, Member member) {
		int activeHolds = 0;
		for (Hold hold : holds) {
			if (hold.getMediaId() == mediaId && hold.getMemberId() == memberId) {
				activeHolds++;
			}
		}
		
		Hold hold = new Hold(nextHoldId++, mediaId, memberId, new Date(), until);
		holds.add(hold);
		numHolds++;
		
		if (member != null) {
			member.getHolds().add(hold);
		}
		System.out.println("Hold successful for member " + memberId +
				". \nTotal holds: " + (activeHolds + 1) + " holds.");
		
		return hold;
	}
	
	public boolean cancelHold(int mediaId, int memberId) {
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
			FileWriter writer = new FileWriter(file);		
			for (Hold h : holds) {
				writer.write("HoldId=" + h.getHoldId() + ", " +
							 "MediaId=" + h.getMediaId()+ ", " +
							 "MediaId=" + h.getMemberId() + ", " +
							 "HoldUntil=" + h.getHoldUntilDate().getTime());
				writer.write("\n");
			}
			
			writer.close();
			System.out.println("Saved hold history.");
		} catch (IOException e) {
			System.out.println("Error saving hold: " + e.getMessage());
		}
	}
	
	public void loadHoldsFromFile(String filename) {
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
				if (str.length != 5) {
					continue;
				}
				
				int holdId = Integer.parseInt(str[0]);
				int mediaId = Integer.parseInt(str[1]);
				int memberId = Integer.parseInt(str[2]);
				long placedMillis = Long.parseLong(str[3]);
				long untilMillis = Long.parseLong(str[4]);
				
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

//				Commented out for now since this function isn't fully ready/tested
				Hold hold = new Hold(holdId, mediaId, memberId, new Date(placedMillis), new Date(untilMillis));
				
				holds.add(hold);
				numHolds++;
			}
		} catch (Exception e) {
			System.out.println("Error reading: " + filename + ": " + e.getMessage());
		}
	}
	
}