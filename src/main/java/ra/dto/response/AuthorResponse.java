package ra.dto.response;

import lombok.Data;

@Data
public class AuthorResponse {
    private int userId;
    private String fullName;
    private String address;
    private int follower;
    private int following;
    private String avatar;
}