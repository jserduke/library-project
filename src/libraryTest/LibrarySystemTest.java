package libraryTest;   

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import library.Library;
import library.LibrarySystem;

public class LibrarySystemTest {

    @Test
    public void constructorShouldInitializeFields() {
        LibrarySystem system = new LibrarySystem("CSUEB Library System");

        assertEquals("CSUEB Library System", system.getName());
        assertNotNull(system.getAccountsDirectory());
        assertNotNull(system.getLibraries());
        assertTrue(system.getLibraries().isEmpty());
    }

    @Test
    public void addLibraryAndGetlibraryShouldWork() {
        LibrarySystem system = new LibrarySystem("Test System");
        Library lib1 = new Library(1, "Main", null);
        Library lib2 = new Library(2, "Branch", null);

        system.addLibrary(lib1);
        system.addLibrary(lib2);

        assertEquals(lib1, system.getlibrary(1));
        assertEquals(lib2, system.getlibrary(2));
        assertNull(system.getlibrary(999)); 
    }

    @Test
    public void setNameShouldUpdateAndReturnNewName() {
        LibrarySystem system = new LibrarySystem("Old Name");

        String returned = system.setName("New Name");

        assertEquals("New Name", returned);
        assertEquals("New Name", system.getName());
    }

    @Test
    public void getLibrariesShouldReturnListWithAddedLibraries() {
        LibrarySystem system = new LibrarySystem("System");
        Library lib = new Library(1, "Main", null);

        assertEquals(0, system.getLibraries().size());

        system.addLibrary(lib);
        List<Library> libs = system.getLibraries();

        assertEquals(1, libs.size());
        assertEquals(lib, libs.get(0));
    }
}
