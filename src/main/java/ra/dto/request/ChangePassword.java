package ra.dto.request;

import lombok.Data;

@Data
public class ChangePassword {
    private String userName;
    private String oldPass;
    private String newPass;
    private int userId;


}