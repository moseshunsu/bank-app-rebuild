package higherAchievers.bankapprebuild.service;

import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.UserRequest;

public interface UserService {
    Response registerUser(UserRequest userRequest);
}
