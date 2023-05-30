package higherAchievers.bankapprebuild.sms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AlertMessage {

    @JsonProperty("alert-message")
    private String message;

}
