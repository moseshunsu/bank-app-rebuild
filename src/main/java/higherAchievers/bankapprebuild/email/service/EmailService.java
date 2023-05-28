package higherAchievers.bankapprebuild.email.service;

import higherAchievers.bankapprebuild.email.dto.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);

}
