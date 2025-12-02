package test.inventoryJUnitTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	test.inventoryJUnitTest.inventoryTest.class,
	test.inventoryJUnitTest.mediaTest.class
})
public class inventoryAllTests {}