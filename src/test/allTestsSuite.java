package test;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import accountTest.*;
import libraryTest.*;
import messageTest.*;
import inventoryTest.*;

@Suite
@SelectClasses({
	inventoryAllTests.class, // inventory Tests
	AllTest.class, // account Tests
	LibrarySystemTest.class, // library Tests
	LibraryTest.class,
	ActionTest.class, // message Tests
	MessageTest.class,
	StatusTest.class,
	TypeTest.class
})

public class allTestsSuite {}
