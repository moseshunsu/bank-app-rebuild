package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Data;
import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.UserRequest;
import higherAchievers.bankapprebuild.entity.User;
import higherAchievers.bankapprebuild.repository.UserRepository;
import higherAchievers.bankapprebuild.utils.ResponseUtils;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public Response fetchUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        User user = userRepository.findById(userId).get();

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        /**
         * check if accNum exists
         * return the balance info
         */
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .build();
    }
}
