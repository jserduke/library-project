package messageTest;

import message.Action;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    @Test
    public void valueOfShouldReturnLoginConstant() {
        Action a = Action.valueOf("LOGIN");
        assertEquals(Action.LOGIN, a);
    }

    @Test
    public void someActionConstantsShouldExist() {
        assertNotNull(Action.LOGIN);
        assertNotNull(Action.LOGOUT);
        assertNotNull(Action.GET_SEARCH); 
    }
}
