package account;

//Created this test driver to make sure my classes were working properly
//import java.io.File;
//import java.util.Date;
//import java.util.Scanner;
//
//public class Main {
//
//    // Temporary dummy DVD class for testing
//    static class DVD {
//        int id;
//        String title;
//
//        public DVD(int id, String title) {
//            this.id = id;
//            this.title = title;
//        }
//
//        public int getId() { return id; }
//
//        @Override
//        public String toString() {
//            return "[DVD] " + id + " - " + title;
//        }
//    }
//
//    public static void main(String[] args) {
//
//        System.out.println("===== TEST: ACCOUNTS DIRECTORY =====");
//
//        AccountsDirectory accounts = new AccountsDirectory();
//
//        Member m1 = (Member) accounts.registerNewAccount(
//                Permission.MEMBER,
//                "alice@test.com",
//                "pass1",
//                "Alice Member",
//                new Date()
//        );
//
//        Admin a1 = (Admin) accounts.registerNewAccount(
//                Permission.ADMIN,
//                "boss@test.com",
//                "admin1",
//                "Bob Admin",
//                new Date()
//        );
//
//        System.out.println(m1);
//        System.out.println(a1);
//
//        System.out.println("\n===== TEST: LOGIN =====");
//        System.out.println("Login result: " + accounts.login("alice@test.com", "pass1"));
//
//        System.out.println("\n===== TEST: DUMMY DVD =====");
//        DVD dvd = new DVD(1001, "The Matrix");
//        System.out.println("Created: " + dvd);
//
//        System.out.println("\n===== TEST: LOAN REPOSITORY =====");
//        LoanRepository loanRepo = new LoanRepository();
//
//        Loan L1 = loanRepo.checkoutMedia(
//                dvd.getId(),
//                m1.getId(),
//                new Date(System.currentTimeMillis() + 5000),
//                m1
//        );
//        System.out.println("Checkout 1: " + L1);
//
//        Loan L2 = loanRepo.checkoutMedia(
//                2002,
//                m1.getId(),
//                new Date(System.currentTimeMillis() + 5000),
//                m1
//        );
//        System.out.println("Checkout 2: " + L2);
//
//        System.out.println("\n===== TEST: LOAN LIMIT =====");
//        Loan denied = loanRepo.checkoutMedia(
//                3003,
//                m1.getId(),
//                new Date(System.currentTimeMillis() + 5000),
//                m1
//        );
//        System.out.println("Checkout 3 (should be null): " + denied);
//
//        System.out.println("\n===== TEST: RETURN + FEES =====");
//        try { Thread.sleep(2000); } catch (Exception e) {}
//        loanRepo.returnMedia(1001, m1.getId());
//        System.out.println("Late Fee: $" + loanRepo.calculateFees(1001, m1.getId()));
//
//        System.out.println("\n===== TEST: HOLDS =====");
//        HoldsRepository holdsRepo = new HoldsRepository();
//        Hold h = holdsRepo.placeHold(
//                555,
//                m1.getId(),
//                new Date(System.currentTimeMillis() + 10000)
//        );
//        System.out.println("Hold: " + h);
//
//
//
//        // ============================================================
//        // FILE WRITE / READ TEST SECTION 
//        // ============================================================
//
//        System.out.println("\n===== FILE WRITE TEST =====");
//
//        // Show working directory
//        System.out.println("Working directory: " + System.getProperty("user.dir"));
//
//        // Save to files
//        loanRepo.saveLoansToFile("loans_test.txt");
//        holdsRepo.saveHoldToFile("holds_test.txt");
//
//        // Check existence
//        File loanFile = new File("loans_test.txt");
//        File holdFile = new File("holds_test.txt");
//
//        if (loanFile.exists()) {
//            System.out.println("Loan file CREATED: " + loanFile.getAbsolutePath());
//        } else {
//            System.out.println("Loan file NOT created.");
//        }
//
//        if (holdFile.exists()) {
//            System.out.println("Hold file CREATED: " + holdFile.getAbsolutePath());
//        } else {
//            System.out.println("Hold file NOT created.");
//        }
//
//
//        // ============================================================
//        //  OPTIONAL: PRINT FILE CONTENTS 
//        // ============================================================
//
//        System.out.println("\n===== FILE CONTENTS: loans_test.txt =====");
//        try (Scanner sc = new Scanner(loanFile)) {
//            while (sc.hasNextLine()) {
//                System.out.println(sc.nextLine());
//            }
//        } catch (Exception e) {
//            System.out.println("Error reading loans_test.txt: " + e.getMessage());
//        }
//
//        System.out.println("\n===== FILE CONTENTS: holds_test.txt =====");
//        try (Scanner sc = new Scanner(holdFile)) {
//            while (sc.hasNextLine()) {
//                System.out.println(sc.nextLine());
//            }
//        } catch (Exception e) {
//            System.out.println("Error reading holds_test.txt: " + e.getMessage());
//        }
//
//        System.out.println("\n===== ALL TESTS COMPLETED SUCCESSFULLY =====");
//    }
//}
