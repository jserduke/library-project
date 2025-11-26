package gui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class LoanUtil {
    private LoanUtil(){}

    public static boolean isReturned(Loan loan){
        return loan.getReturnDate() != null;
    }

    public static int daysLate(Loan loan, LocalDate asOf) {
        if (loan == null) 
        	return 0;
        
        LocalDate due = loan.getDueDate();
        if (due == null) 
        	return 0;

        int grace = 0;
        try {
            grace = loan.getGracePeriodDays();
        } catch (Throwable t) {
            grace = 0;
        }
        LocalDate lateStart = due.plusDays(grace);
        LocalDate end = isReturned(loan) ? loan.getReturnDate() : asOf;
        long days = ChronoUnit.DAYS.between(lateStart, end);
        return (int)Math.max(0, days);
    }

    public static boolean isOverdue(Loan loan, LocalDate asOf){
        return !isReturned(loan) && daysLate(loan, asOf) > 0;
    }
}
