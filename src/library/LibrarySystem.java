package library;
import java.util.ArrayList;
import java.util.List;

import account.AccountsDirectory;

public class LibrarySystem {
	private String name;
	private List<Library> libraries = new ArrayList<>(); // needed to actually hold libraries
	private AccountsDirectory accounts;
	
	public LibrarySystem(String name) {
		this.name = name;
		this.accounts = new AccountsDirectory();
	}
	
	public String getName() {
		return name;
	}
	
	public Library getlibrary(int libraryId) {
		for(Library lib: libraries) {
			if (lib.getId() == libraryId) {
				return lib;
			}
		}
		return null; // throw an exception if not found 
	}
	
	public AccountsDirectory getAccountsDirectory() {
		return accounts;
	}
	
	public String setName(String newName) {
		this.name = newName;
		return this.name;
	}
	
	// helper methods 
	public void addLibrary(Library library) {
		libraries.add(library);
	}
	
	public List<Library> getLibraries() {
		return libraries;
	}

}