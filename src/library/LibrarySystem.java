package library;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
	private String name;
	
	private List<Library> libraries = new ArrayList<>(); // needed to actually hold libraries
	
	public LibrarySystem(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Library getlibrary(Integer libraryId) {
		for(Library lib: libraries) {
			if (lib.getId().equals(libraryId)) {
				return lib;
			}
		}
		return null; // throw an exception if not found 
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