package library;

import inventory.Inventory;

public class Library {
	private int id;
	private String name;
	
	
	private Inventory inventory; 
	
	public Library(int id, String name, Inventory inventory) {
		this.id = id; 
		this.name = name;
		this.inventory = inventory;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public String setName(String newName) {
		this.name = newName;
		return this.name;
	}

}
