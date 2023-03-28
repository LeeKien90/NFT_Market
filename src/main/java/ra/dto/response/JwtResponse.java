package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private int userId;
    private String userName;
    private String email;
    private String avatar;
    private boolean statusUser;

    private List<String> listRoles;
    private String token;

    private String type = "Bearer";

    public JwtResponse(int userId, String token, String userName, String email, String avatar, List<String> listRoles) {
        this.userId = userId;
        this.token = token;
        this.userName = userName;
        this.email = email;
        this.avatar = avatar;

        this.listRoles = listRoles;
    }
}








