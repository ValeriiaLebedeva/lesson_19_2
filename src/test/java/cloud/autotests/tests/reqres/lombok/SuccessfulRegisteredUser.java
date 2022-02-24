package cloud.autotests.tests.reqres.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessfulRegisteredUser {
    private Integer id;
    private String token;
}
