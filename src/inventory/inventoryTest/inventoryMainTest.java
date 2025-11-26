package inventory.inventoryTest;

import java.util.ArrayList;
import inventory.*;

public class inventoryMainTest {
	public static void main(String[] args) {
		Inventory inv = new Inventory(new ArrayList<>());
		inv.addMedia(new Book(2, "BookTitle", "BookPublisher", "BookGenre", 3, 3, "Author", 123.45, "12345"));
		inv.addMedia(new DVD(1, "DVDTitle", "DVDPublisher", "DVDGenre", 2, 2, Rating.PG, 120));
		inv.addMedia(new BoardGame(3, "BoardGameTitle", "BoardGamePublisher", "BoardGameGenre", 0, 2, Rating.G, 2, 4, 30));
		inv.saveInventoryToFile("testInventory");
		
		// New Instance, load from same file saved above
		Inventory inv2 = new Inventory(new ArrayList<>());
		inv2.loadInventoryFromFile("testInventory");
		
		// New instance, load from different file prepared
		Inventory inv3 = new Inventory(new ArrayList<>());
		inv3.loadInventoryFromFile("testInput");
		
		System.out.println("1st Inventory - inv: " + inv.toString());
		System.out.println("2nd Inventory - inv2: " + inv2.toString());
		System.out.println("3rd Inventory - inv3: " + inv3.toString());
		return;
	}
}
