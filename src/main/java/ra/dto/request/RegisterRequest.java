package ra.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class RegisterRequest {
    private String userName;
    private String email;
    private String passwords;
    private String avatar;
    private boolean statusUser;
    private String fullName;
    private String address;
    private Set<String> listRoles;
}
