package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.UserRequest;

import java.util.List;

public interface UserService {
    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long userId);
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
}
