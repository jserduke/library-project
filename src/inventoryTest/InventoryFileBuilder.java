package inventoryTest;
import inventory.*;

import java.util.ArrayList;
import java.util.Scanner;

public class InventoryFileBuilder {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Start IDs from 0 each time we build a new file (optional but nice & clean)
		Media.setNextMediaId(0);
		ArrayList<Media> items = new ArrayList<>();
		
		System.out.println("=== Inventory File Builder ===");
		System.out.println("This will create resources/<filename>.txt");
		System.out.println("Media types: BOOK, DVD, BOARD_GAME");
		System.out.println("Ratings: G, PG, PG_13, NC_17, R, UNRATED");
		System.out.println();

		while (true) {
			System.out.print("Enter media type (BOOK/DVD/BOARD_GAME) or 'done' to finish: ");
			String typeInput = scanner.nextLine().trim();

			if (typeInput.equalsIgnoreCase("done")) {
				break;
			}

			MediaType mediaType;
			try {
				mediaType = MediaType.valueOf(typeInput.toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid media type. Please use BOOK, DVD, or BOARD_GAME.\n");
				continue;
			}
			
			// ----- Optional ID -----
            Integer id = readOptionalInt(scanner, "ID (press Enter for auto): ");
            
			// ----- Base Media fields -----
			System.out.print("Title: ");
			String title = scanner.nextLine().trim();
			System.out.print("Publisher: ");
			String publisher = scanner.nextLine().trim();
			System.out.print("Genre: ");
			String genre = scanner.nextLine().trim();
			int totalQuantity = readInt(scanner, "Total quantity (integer): ");
			int quantityAvailable = readInt(scanner, "Quantity available (integer): ");
			Media item = null;

			// ----- Type-specific fields -----
			switch (mediaType) {
			case BOOK: {
				System.out.print("Author: ");
				String author = scanner.nextLine().trim();
				double ddNumber = readDouble(scanner, "Dewey Decimal number (e.g. 123.45): ");
				System.out.print("ISBN: ");
				String isbn = scanner.nextLine().trim();
				
				// Use ID constructor if provided, else auto-ID constructor
				if (id == null) {
					item = new Book(
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							author,
							ddNumber,
							isbn
							);
				} else {
					item = new Book(
							id,
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							author,
							ddNumber,
							isbn
							);
				}
				break;
			}
			case DVD: {
				Rating rating = readRating(scanner);
				int runTime = readInt(scanner, "Run time in minutes: ");
				if (id == null) {
					item = new DVD(
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							rating,
							runTime
							);
				} else {
					item = new DVD(
							id,
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							rating,
							runTime
							);
				}
				break;
			}
			case BOARD_GAME: {
				Rating rating = readRating(scanner);
				int minPlayers = readInt(scanner, "Minimum players: ");
				int maxPlayers = readInt(scanner, "Maximum players: ");
				int gameLength = readInt(scanner, "Game length in minutes: ");
				if (id == null) {
					item = new BoardGame(
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							rating,
							minPlayers,
							maxPlayers,
							gameLength
							);
				} else {
					item = new BoardGame(
							id,
							title,
							publisher,
							genre,
							totalQuantity,
							quantityAvailable,
							rating,
							minPlayers,
							maxPlayers,
							gameLength
							);
				}
				break;
			}
			default:
				System.out.println("Unsupported type (this should never happen).");
			}

			if (item != null) {
				items.add(item);
				System.out.println("\nAdded item as it will appear in file:");
				System.out.println("Item #" + items.size() + ":" + item.toString());
				System.out.println("Assigned ID: " + item.getId() + "\n");
			}
		}

		if (items.isEmpty()) {
			System.out.println("\nNo items entered. Nothing to save.");
			scanner.close();
			return;
		}

		System.out.print("\nEnter output filename (without .txt, default = inventory): ");
		String filename = scanner.nextLine().trim();
		if (filename.isEmpty()) {
			filename = "inventory";
		}

		Inventory inventory = new Inventory(items);
		inventory.saveInventoryToFile(filename);

		System.out.println("Done!");
		System.out.println("File written to: resources/" + filename + ".txt");
		System.out.println("Load with: inventory.loadInventoryFromFile(\"" + filename + "\");");

		scanner.close();
	}

	// ----- Helpers -----

	private static Integer readOptionalInt(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			if (line.isEmpty()) {
				return null; // auto ID
			}
			try {
				return Integer.valueOf(line);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer or press Enter to skip.");
			}
		}
	}
	
	private static int readInt(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			try {
				return Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer.");
			}
		}
	}

	private static double readDouble(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			try {
				return Double.parseDouble(line);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid decimal number.");
			}
		}
  	}

	private static Rating readRating(Scanner scanner) {
		while (true) {
			System.out.print("Age rating (G, PG, PG_13, NC_17, R, UNRATED): ");
			String input = scanner.nextLine().trim().toUpperCase();
			// Allow PG-13 / NC-17 style too:
			input = input.replace('-', '_');
			try {
				return Rating.valueOf(input);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid rating. Try again.");
			}
		}
	}
}
