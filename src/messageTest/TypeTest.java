package messageTest;

import message.Type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TypeTest {

    @Test
    public void valueOfShouldReturnRequestConstant() {
        Type t = Type.valueOf("REQUEST");
        assertEquals(Type.REQUEST, t);
    }

    @Test
    public void typeConstantsShouldExist() {
        assertNotNull(Type.REQUEST);
        assertNotNull(Type.RESPONSE);
    }
}
