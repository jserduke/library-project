package accountTest;

import org.junit.platform.suite.api.SelectClasses;

import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	AccountTest.class,
    AccountsDirectoryTest.class,
    AdminTest.class,
    HoldTest.class,
    LoanTest.class,
    MemberTest.class,
    HoldsRepositoryTest.class,
    LoanRepositoryTest.class
})

public class AllTest {
	
}
