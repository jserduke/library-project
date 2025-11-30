package messageTest;

import message.Status;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatusTest {

    @Test
    public void valueOfShouldReturnSuccessConstant() {
        Status s = Status.valueOf("SUCCESS");
        assertEquals(Status.SUCCESS, s);
    }

    @Test
    public void statusConstantsShouldExist() {
        assertNotNull(Status.PENDING);
        assertNotNull(Status.SUCCESS);
        assertNotNull(Status.FAILURE);
    }
}
