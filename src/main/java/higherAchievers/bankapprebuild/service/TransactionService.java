package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
