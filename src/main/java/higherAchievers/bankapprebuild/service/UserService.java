package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.TransactionRequest;
import higherAchievers.bankapprebuild.dto.TransferRequest;
import higherAchievers.bankapprebuild.dto.UserRequest;
import higherAchievers.bankapprebuild.email.dto.EmailDetails;
import higherAchievers.bankapprebuild.entity.AlertType;
import higherAchievers.bankapprebuild.entity.User;
import higherAchievers.bankapprebuild.sms.domain.AlertMessage;
import higherAchievers.bankapprebuild.sms.domain.Recipient;
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
    EmailDetails emailDetails(AlertType alert, AlertType alert1, User user, TransactionRequest transactionRequest);
    AlertMessage alertMessage(AlertType alert, User user, TransactionRequest transactionRequest);
    Recipient recipient(User user);
}
