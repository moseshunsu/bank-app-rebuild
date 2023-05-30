package higherAchievers.bankapprebuild.sms.service;

import higherAchievers.bankapprebuild.sms.domain.AlertMessage;
import higherAchievers.bankapprebuild.sms.domain.Recipient;

public interface AlertService {

    void sendSMS(Recipient recipient, AlertMessage alertMessage);

}
