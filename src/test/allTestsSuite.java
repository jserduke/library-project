package test;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import accountTest.*;
import libraryTest.*;
import messageTest.*;
import inventoryTest.*;

@Suite
@SelectClasses({
	inventoryTest.class,
	mediaTest.class,
	AccountsDirectoryTest.class,
	AccountTest.class,
	AdminTest.class,
	HoldsRepositoryTest.class,
	HoldTest.class,
	LoanRepositoryTest.class,
	LoanTest.class,
	MemberTest.class,
	LibrarySystemTest.class,
	LibraryTest.class,
	ActionTest.class,
	MessageTest.class,
	StatusTest.class,
	TypeTest.class
})

public class allTestsSuite {}
