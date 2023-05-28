package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.*;
import higherAchievers.bankapprebuild.email.dto.EmailDetails;
import higherAchievers.bankapprebuild.email.service.EmailService;
import higherAchievers.bankapprebuild.entity.AlertType;
import higherAchievers.bankapprebuild.entity.User;
import higherAchievers.bankapprebuild.repository.UserRepository;
import higherAchievers.bankapprebuild.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

    private UserRepository userRepository;
    private TransactionService transactionService;
    private EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        /**
         * check if user exists, if the usr doesn't exist return response,
         * generate account number
         * go ahead to save
         */
        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());

        if (isEmailExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();

        }


        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();
        User savedUser = userRepository.save(user);


        EmailDetails emailDetails = EmailDetails.builder()
                .subject("ACCOUNT CREATION")
                .recipient(userRequest.getEmail())
                .messageBody(
                        "Dear " + userRequest.getFirstName() + " " + " " + userRequest.getOtherName() + " " +
                                userRequest.getLastName() +
                                ", your account has been successful created and your balance is N"
                                + userRequest.getAccountBalance() + "." +
                                "\n\nKindly note that this is demo mail, and that it exists sorely for test " +
                                "purposes." +
                                "\n\n\nBest Regards, \n\nMoses Hunsu."
                )
                .attachment("C:\\Users\\moses.hunsu\\Documents\\Account test demo.txt")
                .build();

        emailService.sendMailWithAttachment(emailDetails);


        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build()
                )
                .build();
    }

    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();

        List<Response> response = new ArrayList<>();
        for (User user: usersList) {
            response.add(Response.builder()
                    .responseCode(ResponseUtils.SUCCESS)
                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                            .data(Data.builder()
                                    .accountNumber(user.getAccountNumber())
                                    .accountBalance(user.getAccountBalance())
                                    .accountName(user.getFirstName() + " " + user.getLastName())
                                    .build())
                    .build());
        }
        return response;
    }

    @Override
    public ResponseEntity<Response> fetchUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(userId).get();

        return new ResponseEntity<>(Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Response> balanceEnquiry(String accountNumber) {
        /**
         * check if accNum exists
         * return the balance info
         */
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByAccountNumber(accountNumber);

        return new ResponseEntity<>(Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> nameEnquiry(String accountNumber) {
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findByAccountNumber(accountNumber);

        return new ResponseEntity<>(Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> credit(TransactionRequest transactionRequest) {
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .build())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(receivingUser);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);

        emailService.sendSimpleMail(emailDetails(AlertType.CREDIT, AlertType.credited, receivingUser,
                transactionRequest));

        return new ResponseEntity<>(Response.builder()
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getLastName())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountNumber(receivingUser.getAccountNumber())
                        .build())
                .build(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Response> debit(TransactionRequest transactionRequest) {
        User debitingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .build())
                    .build(), HttpStatus.NOT_FOUND);
        }

        if (debitingUser.getAccountBalance().compareTo(transactionRequest.getAmount()) > 0) {

            debitingUser.setAccountBalance(debitingUser.getAccountBalance().subtract
                    (transactionRequest.getAmount()));
            userRepository.save(debitingUser);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setAmount(transactionRequest.getAmount());
            transactionDto.setTransactionType("Debit");
            transactionDto.setAccountNumber(transactionRequest.getAccountNumber());

            transactionService.saveTransaction(transactionDto);

            emailService.sendSimpleMail(emailDetails(AlertType.DEBIT, AlertType.debited, debitingUser,
                    transactionRequest));


            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                    .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                    .data(Data.builder()
                            .accountName(debitingUser.getFirstName() + " " + debitingUser.getLastName())
                            .accountBalance(debitingUser.getAccountBalance())
                            .accountNumber(debitingUser.getAccountNumber())
                            .build())
                    .build(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(Response.builder()
                .responseMessage(ResponseUtils.UNSUCCESSFUL_TRANSACTION)
                .responseCode(ResponseUtils.INSUFFICIENT_BALANCE)
                .data(Data.builder()
                        .accountName(debitingUser.getFirstName() + " " + debitingUser.getLastName())
                        .accountBalance(debitingUser.getAccountBalance())
                        .accountNumber(debitingUser.getAccountNumber())
                        .build())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Response> transfer(TransferRequest transferRequest) {

        boolean receiverExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (receiverExist) {

            ResponseEntity<Response> debitResponse = debit(new TransactionRequest(transferRequest.getSourceAccountNumber(),
                    transferRequest.getAmount()));

            if (debitResponse.getStatusCode() != HttpStatus.ACCEPTED) {
                return debitResponse;
            }

            credit(new TransactionRequest(transferRequest.getDestinationAccountNumber(), transferRequest.getAmount()));
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.SUCCESS)
                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(Response.builder()
                .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                .data(Data.builder()
                        .accountNumber(transferRequest.getDestinationAccountNumber())
                        .build())
                .build(), HttpStatus.BAD_REQUEST);

    }

    @Override
    public EmailDetails emailDetails(AlertType alert, AlertType alert1, User user,
                                     TransactionRequest transactionRequest) {

        return EmailDetails
                .builder()
                .recipient(user.getEmail())
                .subject(alert + " ALERT")
                .messageBody(
                        "Dear " + user.getFirstName() + " " + " " + user.getOtherName() + " " +
                                user.getLastName() +
                                ", your account has been " + alert1 +
                                " with N" + transactionRequest.getAmount() +
                                ", and your account balance is N"
                                + user.getAccountBalance() + "." +
                                "\n\nKindly note that this is demo mail, and that it exists sorely for test " +
                                "purposes." +
                                "\n\n\nBest Regards, \n\nMoses Hunsu."
                )
                .build();


    }

}

