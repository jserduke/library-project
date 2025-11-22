package gui;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryData {
    public static final List<User> USERS = new ArrayList<>();
    public static final List<Member> MEMBERS = new ArrayList<>();
    public static final List<Book> BOOKS = new ArrayList<>();
    public static final List<Dvd> DVDS = new ArrayList<>();
    public static final List<BoardGame> BOARD_GAMES = new ArrayList<>();
    public static final List<Loan> LOANS = new ArrayList<>();
    public static final List<Hold> HOLDS = new ArrayList<>();

    // --------- Auth / lookups ------------------------------------------------
    // Find a user by username + password (case-insensitive username)
    public static User findUser(String username, String password) {
        if (username == null || password == null) 
        	return null;
        for (User u : USERS) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static Member findMemberByEmail(String email) {
        for (Member m : MEMBERS) 
        	if (m.getEmail().equalsIgnoreCase(email)) 
        		return m;
        return null;
    }

    public static User findUserByUsername(String username) {
        for (User u : USERS) 
        	if (u.getUsername().equalsIgnoreCase(username)) 
        		return u;
        return null;
    }

    // --------- ID helpers ----------------------------------------------------
    public static int nextMemberId() { 
    	int max = 0; 
    	for (Member m : MEMBERS) 
    		max = Math.max(max, m.getId()); 
    	return max + 1; 
    }
    public static int nextBookId() { 
    	return BOOKS.stream().mapToInt(Book::getId).max().orElse(0) + 1;
    }
    public static int nextDvdId()  { 
    	return DVDS.stream().mapToInt(Dvd::getId).max().orElse(0) + 1; 
    }
    public static int nextBoardGameId() { 
    	return BOARD_GAMES.stream().mapToInt(BoardGame::getId).max().orElse(0) + 1; 
    }
    public static int nextLoanId() { 
    	int max = 0; 
    	for (Loan l : LOANS) 
    		max = Math.max(max, l.getId()); 
    	return max + 1; 
    }


    // --------- Member activity ----------------------------------------------
    // All loans for a member
    public static List<Loan> loansForMember(int memberId) {
        List<Loan> list = new ArrayList<>();
        for (Loan l : LOANS) 
        	if (l.getMemberId() == memberId) 
        		list.add(l);
        return list;
    }

    // Checkout: create a loan if a copy is available; otherwise return null
    public static Loan checkout(int memberId, MediaType type, int mediaId) {
        int available;
        switch (type) {
            case BOOK       -> available = availableCountForBook(mediaId);
            case DVD        -> available = availableCountForDvd(mediaId);
            case BOARD_GAME -> available = availableCountForBoardGame(mediaId);
            default         -> available = 0;
        }
        if (available <= 0) 
        	return null;

        LocalDate today = LocalDate.now();
        LocalDate due   = today.plusDays(21);   // 3-week loan
        int grace       = 5;                    // 5-day grace period
        Loan loan = new Loan(nextLoanId(), memberId, type, mediaId, today, due, grace);
        LOANS.add(loan);
        return loan;
    }

    // Return a loan by id
    public static boolean returnLoan(int loanId) {
        for (Loan l : LOANS) {
            if (l.getId() == loanId && !l.isReturned()) {
                l.setReturnDate(LocalDate.now());
                return true;
            }
        }
        return false;
    }

    // Place a hold until a specific date
    public static Hold placeHold(int memberId, MediaType type, int mediaId, LocalDate holdUntil) {
        Hold h = new Hold(nextHoldId(), memberId, type, mediaId, holdUntil);
        HOLDS.add(h);
        return h;
    }

    // --------- Seed data -----------------------------------------------------
    static {
        // Users
        if (USERS.isEmpty()) {
            USERS.add(new User("admin", "admin123", User.Role.ADMIN));
            USERS.add(new User("member", "member123", User.Role.USER));
        }
        // Members
        if (MEMBERS.isEmpty()) {
            MEMBERS.add(new Member(1, "Ada", "Lovelace", LocalDate.of(1815, 12, 10), "555-1000", "ada@example.com"));
            MEMBERS.add(new Member(2, "Alan", "Turing",   LocalDate.of(1912,  6, 23), "555-2000", "alan@example.com"));
        }
        // Books 
        if (BOOKS.isEmpty()) {
            BOOKS.add(new Book(1,"9780134685991","Effective Java","Joshua Bloch","Addison-Wesley","Programming",4));
            BOOKS.add(new Book(2,"9781491950357","Designing Data-Intensive Applications","Martin Kleppmann","O'Reilly","Databases",3));
            BOOKS.add(new Book(3,"9780132350884","Clean Code","Robert C. Martin","Prentice Hall","Programming",5));
            BOOKS.add(new Book(4,"9780134494166","Clean Architecture","Robert C. Martin","Pearson","Programming",2));
            BOOKS.add(new Book(5,"9780201633610","Design Patterns","Gamma et al.","Addison-Wesley","Programming",3));
            BOOKS.add(new Book(6,"9780131103627","The C Programming Language","Kernighan & Ritchie","Prentice Hall","Programming",2));
            BOOKS.add(new Book(7,"9780596007126","Head First Design Patterns","Eric Freeman","O'Reilly","Programming",3));
            BOOKS.add(new Book(8,"9780262033848","Introduction to Algorithms","Cormen et al.","MIT Press","Algorithms",4));
        }
        // DVDs
        if (DVDS.isEmpty()) {
            DVDS.add(new Dvd(1,"The Matrix","R",136,5).studio("Warner Bros"));
            DVDS.add(new Dvd(2,"Interstellar","PG-13",169,2).studio("Paramount"));
            DVDS.add(new Dvd(3,"Inception","PG-13",148,3).studio("Warner Bros"));
            DVDS.add(new Dvd(4,"Arrival","PG-13",116,4).studio("Paramount"));
            DVDS.add(new Dvd(5,"Wall-E","G",98,3).studio("Pixar"));
            DVDS.add(new Dvd(6,"The Lord of the Rings: The Fellowship of the Ring","PG-13",178,3).studio("New Line"));
        }
        // Board games
        if (BOARD_GAMES.isEmpty()) {
            BOARD_GAMES.add(new BoardGame(1,"Catan","7.2","3-4",90,3));
            BOARD_GAMES.add(new BoardGame(2,"Ticket to Ride","7.4","2-5",60,4));
            BOARD_GAMES.add(new BoardGame(3,"Pandemic","7.6","2-4",45,3));
            BOARD_GAMES.add(new BoardGame(4,"Carcassonne","7.4","2-5",35,3));
            BOARD_GAMES.add(new BoardGame(5,"Gloomhaven","8.8","1-4",120,2));
            BOARD_GAMES.add(new BoardGame(6,"Codenames","7.7","2-8",15,5));
        }
    }

    // --------- Search --------------------------------------------------------
    public static List<Book> searchBooks(String q) {
        String s = q.toLowerCase();
        return BOOKS.stream().filter(b ->
            b.getTitle().toLowerCase().contains(s) ||
            b.getAuthor().toLowerCase().contains(s) ||
            b.getIsbn().toLowerCase().contains(s) ||
            b.getPublisher().toLowerCase().contains(s)
        ).collect(Collectors.toList());
    }

    public static List<Dvd> searchDvds(String q) {
        String s = q.toLowerCase();
        return DVDS.stream().filter(d ->
            d.getTitle().toLowerCase().contains(s) ||
            d.getRating().toLowerCase().contains(s)
        ).collect(Collectors.toList());
    }

    public static List<BoardGame> searchBoardGames(String q) {
        String s = q.toLowerCase();
        return BOARD_GAMES.stream().filter(g ->
            g.getTitle().toLowerCase().contains(s) ||
            g.getRating().toLowerCase().contains(s) ||
            g.getPlayerCount().toLowerCase().contains(s)
        ).collect(Collectors.toList());
    }

    // --------- Availability (based on open loans) ---------------------------
    public static int availableCountForBook(int bookId) {
        int total = BOOKS.stream().filter(b -> b.getId()==bookId).findFirst().map(Book::getTotalQuantity).orElse(0);
        long onLoan = LOANS.stream().filter(l -> l.getMediaType()==MediaType.BOOK && l.getMediaId()==bookId && !l.isReturned()).count();
        return Math.max(total - (int)onLoan, 0);
    }

    public static int availableCountForDvd(int dvdId) {
        int total = DVDS.stream().filter(d -> d.getId()==dvdId).findFirst().map(Dvd::getTotalQuantity).orElse(0);
        long onLoan = LOANS.stream().filter(l -> l.getMediaType()==MediaType.DVD && l.getMediaId()==dvdId && !l.isReturned()).count();
        return Math.max(total - (int)onLoan, 0);
    }

    public static int availableCountForBoardGame(int gameId) {
        int total = BOARD_GAMES.stream().filter(g -> g.getId()==gameId).findFirst().map(BoardGame::getTotalQuantity).orElse(0);
        long onLoan = LOANS.stream().filter(l -> l.getMediaType()==MediaType.BOARD_GAME && l.getMediaId()==gameId && !l.isReturned()).count();
        return Math.max(total - (int)onLoan, 0);
    }

    // --------- Admin helpers -------------------------------------------------
    public static void addBook(Book b) { 
    	BOOKS.add(b); 
    }
    public static void addDvd(Dvd d) {
    	DVDS.add(d); 
    }
    public static void addBoardGame(BoardGame g) { 
    	BOARD_GAMES.add(g); 
    }

    public static boolean removeBookById(int id) { 
    	return BOOKS.removeIf(b -> b.getId()==id); 
    }
    public static boolean removeDvdById(int id) {
    	return DVDS.removeIf(d -> d.getId()==id); 
    }
    public static boolean removeBoardGameById(int id) {
    	return BOARD_GAMES.removeIf(g -> g.getId()==id); 
    }


// ======== Late fee store & policy ========
public static final List<LateFee> LATE_FEES = new ArrayList<>();
public static int LATE_FEE_PER_DAY_CENTS = 100;    // $1/day
public static int LATE_FEE_MAX_CENTS = 2000;       // $20 cap
public static int ACCOUNT_FREEZE_THRESHOLD_CENTS = 5000; // optional block

public static int nextLateFeeId() {
    int max = 0;
    for (LateFee f : LATE_FEES) max = Math.max(max, f.getId());
    return max + 1;
}
public static LateFee findLateFeeById(int id) {
    for (LateFee f : LATE_FEES) 
    	if (f.getId()==id) 
    		return f;
    return null;
}
public static int memberBalanceCents(int memberId) {
    int sum = 0;
    for (LateFee f : LATE_FEES) {
        if (f.getMemberId()==memberId && f.getStatus()==LateFee.Status.PENDING) sum += f.getAmountCents();
    }
    return sum;
}
public static void markFeePaid(int feeId, LocalDate when) {
    LateFee fee = findLateFeeById(feeId);
    if (fee != null) 
    	fee.markPaid(when);
}
public static void waiveFee(int feeId, String reason) {
    LateFee fee = findLateFeeById(feeId);
    if (fee != null) 
    	fee.waive(reason);
}

// Assess a late fee for a loan on check-in, if any is owed, and return the LateFee object (or null).

public static LateFee assessLateFeeForLoan(Loan loan, LocalDate now) {
    int days = loan.computeDaysLate(now);
    if (days <= 0) 
    	return null;
    int amount = loan.computeLateFeeCents(LATE_FEE_PER_DAY_CENTS, LATE_FEE_MAX_CENTS, now);
    LateFee fee = new LateFee(nextLateFeeId(), loan.getId(), loan.getMemberId(), days, amount, now);
    LATE_FEES.add(fee);
    return fee;
}

// ======== Hold helpers (queuing, activation, expiration) ========
public static int nextHoldId() {
    int max = 0;
    for (Hold h : HOLDS) max = Math.max(max, h.getId());
    return max + 1;
}


// Place a hold in REQUESTED status (queued).
// Returns the new Hold object.
public static Hold placeHold(int memberId, MediaType type, int mediaId) {
    int position = (int) HOLDS.stream()
            .filter(h -> h.getMediaType()==type && h.getMediaId()==mediaId && h.getStatus()==Hold.HoldStatus.REQUESTED)
            .count() + 1;
    Hold h = new Hold(nextHoldId(), 
    		memberId, 
    		type, 
    		mediaId, 
    		Hold.HoldStatus.REQUESTED,
            LocalDate.now(), 
            null, 
            null, 
            position, 
            null);
    HOLDS.add(h);
    return h;
}


// Activate the next REQUESTED hold (used when an item is returned to shelf).

public static Hold activateNextHoldFor(MediaType type, int mediaId, int holdWindowHours) {
    Hold next = HOLDS.stream()
            .filter(h -> h.getMediaType()==type && h.getMediaId()==mediaId && h.getStatus()==Hold.HoldStatus.REQUESTED)
            .sorted(Comparator.comparing(Hold::getQueuePosition, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Hold::getPlacedAt))
            .findFirst().orElse(null);
    
    if (next == null) 
    	return null;
    LocalDate today = LocalDate.now();
    LocalDate deadline = today.plusDays(Math.max(1, holdWindowHours/24));
    next.setStatus(Hold.HoldStatus.ACTIVE);
    next.setActivatedAt(today);
    next.setPickupDeadline(deadline);
    return next;
}


// Mark ACTIVE holds as EXPIRED if past pickupDeadline.

public static void expireOverdueHolds(LocalDate today) {
    for (Hold h : HOLDS) {
        if (h.isExpired(today)) {
            h.setStatus(Hold.HoldStatus.EXPIRED);
        }
    }
}


// Utility to get active holds.

public static List<Hold> activeHolds() {
    return HOLDS.stream().filter(Hold::isActive).collect(Collectors.toList());
}

}
