package cloud.autotests.tests.reqres.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {
    private Integer id;
    private String name;
    private String job;
    private String createdAt;
}