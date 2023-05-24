package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.TransactionRequest;
import higherAchievers.bankapprebuild.dto.TransferRequest;
import higherAchievers.bankapprebuild.dto.UserRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    ResponseEntity<Response> fetchUser(Long userId);
    ResponseEntity<Response> balanceEnquiry(String accountNumber);
    ResponseEntity<Response> nameEnquiry(String accountNumber);
    ResponseEntity<Response> credit(TransactionRequest transactionRequest);
    ResponseEntity<Response> debit(TransactionRequest transactionRequest);
    ResponseEntity<Response> transfer(TransferRequest transferRequest);
}
