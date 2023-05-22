package higherAchievers.bankapprebuild.repository;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String AccountNumber);
    User findByAccountNumber(String AccountNumber);
}
