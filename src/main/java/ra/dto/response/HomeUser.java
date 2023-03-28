package ra.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class HomeUser {
    private int authorId;
    private String authorName;
    private String avatar;
    private String userName;
}
