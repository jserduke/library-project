package gui;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class LateFeeService {
    private LateFeeService(){}

    public static int projectedFeeCents(Loan loan, LocalDate asOf){
        if (loan == null) return 0;
        int days = LoanUtil.daysLate(loan, asOf);
        if (days <= 0) return 0;
        int perDay = FeePolicy.PER_DAY_CENTS;
        int amount = perDay * days;
        if (FeePolicy.MAX_PER_LOAN_CENTS > 0){
            amount = Math.min(amount, FeePolicy.MAX_PER_LOAN_CENTS);
        }
        return amount;
    }

    /** Assess and store a LateFee when a loan is returned after the due date + grace. */
    public static LateFee assessOnReturn(Loan loan){
        if (loan == null) return null;
        int days = LoanUtil.daysLate(loan, LocalDate.now());
        if (days <= 0) return null;
        int amount = projectedFeeCents(loan, LocalDate.now());
        LateFee fee = new LateFee(
                FeeStore.nextLateFeeId(),
                loan.getId(),
                loan.getMemberId(),
                loan.getMediaType(),
                loan.getMediaId(),
                LocalDateTime.now(),
                days,
                amount,
                LateFee.Status.PENDING
        );
        FeeStore.LATE_FEES.add(fee);
        return fee;
    }
}
