package LibraryGUI;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public final class FeeStore {
    private FeeStore(){}

    public static final List<LateFee> LATE_FEES = new ArrayList<>();
    public static final List<Payment> PAYMENTS = new ArrayList<>();

    private static int LATE_FEE_SEQ = 1;
    private static int PAYMENT_SEQ = 1;

    public static synchronized int nextLateFeeId(){ 
    	return LATE_FEE_SEQ++; 
    }
    public static synchronized int nextPaymentId(){
    	return PAYMENT_SEQ++; 
    }

    public static List<LateFee> feesForMember(int memberId){
        return LATE_FEES.stream().filter(f -> f.getMemberId() == memberId).collect(Collectors.toList());
    }

    public static int balanceCentsForMember(int memberId){
        int pending = LATE_FEES.stream()
                .filter(f -> f.getMemberId()==memberId && f.getStatus()== LateFee.Status.PENDING)
                .mapToInt(LateFee::getAmountCents).sum();
        return pending;
    }

    public static LateFee markPaid(int feeId){
        for (LateFee f : LATE_FEES){
            if (f.getId()==feeId){
                f.setStatus(LateFee.Status.PAID);
                f.setPaidAt(LocalDateTime.now());
                return f;
            }
        }
        return null;
    }

    public static LateFee waive(int feeId, String reason){
        for (LateFee f : LATE_FEES){
            if (f.getId()==feeId){
                f.setStatus(LateFee.Status.WAIVED);
                f.setWaivedReason(reason);
                return f;
            }
        }
        return null;
    }
}
