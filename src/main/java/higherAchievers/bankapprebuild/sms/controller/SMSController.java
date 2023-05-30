package higherAchievers.bankapprebuild.sms.controller;

import higherAchievers.bankapprebuild.sms.domain.AlertMessage;
import higherAchievers.bankapprebuild.sms.domain.Recipient;
import higherAchievers.bankapprebuild.sms.service.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "SMS Service REST APIs/Endpoint",
        description = "Endpoints for Manipulating SMS Services"
)
public class SMSController {

    @Autowired
    private AlertService alertService;

    @PostMapping("api/sms")
    public void sendSMS(@RequestBody AlertMessage alertMessage) {
        Recipient recipient = new Recipient("...", "...", "+23....");
        alertService.sendSMS(recipient, alertMessage);
    }

}
