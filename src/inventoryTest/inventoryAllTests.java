package inventoryTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	inventoryTest.class,
	mediaTest.class
})
public class inventoryAllTests {}