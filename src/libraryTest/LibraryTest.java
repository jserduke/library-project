package libraryTest;   

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import library.Library;

public class LibraryTest {

    @Test
    public void constructorShouldSetFields() {
        Library lib = new Library(1, "Main Library", null);

        assertEquals(1, lib.getId());
        assertEquals("Main Library", lib.getName());
        assertNull(lib.getInventory()); 
    }

    @Test
    public void setNameShouldUpdateAndReturnNewName() {
        Library lib = new Library(1, "Old Name", null);

        String returned = lib.setName("New Name");

        assertEquals("New Name", returned);
        assertEquals("New Name", lib.getName());
    }
}
