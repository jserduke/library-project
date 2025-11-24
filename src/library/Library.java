package library;

import inventory.Inventory;

public class Library {
	private Integer id;
	private String name;
	
	
	private Inventory inventory; 
	
	public Library(Integer id, String name, Inventory inventory) {
		this.id = id; 
		this.name = name;
		this.inventory = inventory;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public String setName(String newName) {
		this.name = newName;
		return this.name;
	}

}
