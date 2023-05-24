package higherAchievers.bankapprebuild.controller;


import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.TransactionRequest;
import higherAchievers.bankapprebuild.dto.TransferRequest;
import higherAchievers.bankapprebuild.dto.UserRequest;
import higherAchievers.bankapprebuild.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> registerUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.registerUser(userRequest), HttpStatus.CREATED) ;
    }

    @GetMapping
    public List<Response> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> fetchUser(@PathVariable (name = "userId") Long userId) {
        return userService.fetchUser(userId);
    }

    @GetMapping("/balanceEnquiry")
    public ResponseEntity<Response> balanceEnquiry(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public ResponseEntity<Response> nameEnquiry(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }

    @PutMapping("/credit")
    public ResponseEntity<Response> credit(@RequestBody TransactionRequest transactionRequest) {
        return userService.credit(transactionRequest);
    }

    @PutMapping("/debit")
    public ResponseEntity<Response> debit(@RequestBody TransactionRequest transactionRequest) {
        return userService.debit(transactionRequest);
    }

    @PutMapping("/transfer")
    public ResponseEntity<Response> transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

}
