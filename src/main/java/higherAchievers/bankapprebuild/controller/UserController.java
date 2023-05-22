package higherAchievers.bankapprebuild.controller;


import higherAchievers.bankapprebuild.dto.Response;
import higherAchievers.bankapprebuild.dto.UserRequest;
import higherAchievers.bankapprebuild.service.UserService;
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
    public Response registerUser(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public Response fetchUser(@PathVariable (name = "userId") Long userId) {
        return userService.fetchUser(userId);
    }

    @GetMapping("/balanceEnquiry")
    public Response balanceEnquiry(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response nameEnquiry(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }
}
