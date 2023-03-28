package ra.dto.response;

import lombok.Data;

import java.util.Date;
@Data
public class BlogReponse {
    private int blogID;
    private String blogTitle;
    private String blogContent;
    private String blogImage;
    private Date createdDate;
    private int views;
    private String authorName;
    private int authorId;
}
