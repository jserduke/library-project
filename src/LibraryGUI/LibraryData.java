package LibraryGUI;

import java.util.ArrayList;
import java.util.List;


public class LibraryData {

	public static final List<User>   USERS   = new ArrayList<>();
	public static final List<Member> MEMBERS = new ArrayList<>();
	public static final List<Book>   BOOKS   = new ArrayList<>();

	// simple genre/author lists so the extra windows have something to show
	public static final List<String> GENRES  = new ArrayList<>();
	public static final List<String> AUTHORS = new ArrayList<>();

	static {
		// demo users
		USERS.add(new User("user",  "1234", User.Role.USER));
		USERS.add(new User("admin", "admin", User.Role.ADMIN));

		// demo members
		MEMBERS.add(new Member(1, "joe",   "potato", "555-123", "joe@mail.com",   "Male"));
		MEMBERS.add(new Member(2, "carle", "banana", "555-456", "carle@mail.com", "Female"));

		// demo books
		BOOKS.add(new Book(1, "AAA111", "the ultimate book of programming",
             "joe doe", "programming", 5, true));
		BOOKS.add(new Book(2, "BBB222", "money is good just like food",
             "banana", "finance", 3, false));
		BOOKS.add(new Book(3, "CCC333", "learn java in one minute",
             "smart kid", "programming", 2, true));

		// demo genres & authors
		GENRES.add("programming");
		GENRES.add("history");
		GENRES.add("math");
		AUTHORS.add("joe doe");
     	AUTHORS.add("carle banana");
	}

	public static int nextMemberId() {
		int max = 0;
		for (Member m : MEMBERS) 
			if (m.getId() > max) max = m.getId();
		return max + 1;
	}

	public static int nextBookId() {
		int max = 0;
		for (Book b : BOOKS) 
			if (b.getId() > max) max = b.getId();
		return max + 1;
	}
}
