package higherAchievers.bankapprebuild.repository;

import higherAchievers.bankapprebuild.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
