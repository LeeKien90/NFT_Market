package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "commentID")
    private int commentID;
    @JoinColumn(name = "commentDate")
    private Date createdDate;
    @JoinColumn(name = "commentContent")
    private String comment;
    @JoinColumn(name = "commentStatus")
    private boolean commentStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "userID")
    private Users users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "blogID")
    private Blog blog;
}
