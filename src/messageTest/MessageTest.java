package messageTest;

import message.Action;
import message.Message;
import message.Status;
import message.Type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MessageTest {

    @Test
    public void constructorAndGettersShouldSetFieldsCorrectly() {
        int libraryId = 123;
        Type type = Type.REQUEST;
        int requestId = 42;
        Action action = Action.LOGIN;
        Status status = Status.PENDING;
        ArrayList<String> info = new ArrayList<>();
        info.add("userId=10");
        info.add("extra=data");

        Message msg = new Message(type, requestId, action, status, info);

        // assertEquals(libraryId, msg.getLibraryId());
        assertEquals(type, msg.getType());
        assertEquals(requestId, msg.getRequestId());
        assertEquals(action, msg.getAction());
        assertEquals(status, msg.getStatus());
        assertEquals(info, msg.getInfo());
        assertNotNull(msg.getDatetime());  
    }

    @Test
    public void toStringShouldContainKeyFields() {
        ArrayList<String> info = new ArrayList<>();
        info.add("hello");

        Message msg = new Message(
                Type.REQUEST,
                99,
                Action.GET_SEARCH,
                Status.SUCCESS,
                info
        );

        String s = msg.toString();

        assertTrue(s.contains("type: REQUEST"));
        assertTrue(s.contains("request id: 99"));
        assertTrue(s.contains("status: SUCCESS"));
        assertTrue(s.contains("action: GET_SEARCH"));
        assertTrue(s.contains("info: [hello]"));
    }
}
