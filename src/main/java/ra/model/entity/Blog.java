package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "blogID")
    private int blogID;
    @JoinColumn(name = "blogTitle")
    private String blogTitle;
    @JoinColumn(name = "blogContent")
    private String blogContent;
    @JoinColumn(name = "blogImage")
    private String blogImage;
    @JoinColumn(name = "blogCreateDate")
    private Date createdDate;
    @JoinColumn(name = "views")
    private int views;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "userCreatedID")
    private Users users;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "blogtag", joinColumns = @JoinColumn(name = "blogID"), inverseJoinColumns = @JoinColumn(name = "tagID"))
    private Set<Tag> listTag = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "likes", joinColumns = @JoinColumn(name = "blogID"), inverseJoinColumns = @JoinColumn(name = "userID"))
    private Set<Users> listLikeUser = new HashSet<>();

}
