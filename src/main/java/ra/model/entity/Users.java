package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Table(name = "User")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private int userId;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "userEmail")
    private String email;
    @Column(name = "userPassword")
    private String passwords;
    @Column(name = "userAvatar")
    private String avatar;
    @Column(name = "userStatus")
    private boolean statusUser;
    @JoinColumn(name = "fullName")
    private String fullName;
    @Column(name = "address")
    private String address;
    @JoinColumn(name = "created")
    private LocalDate created;
    //tạo bảng user_role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Roles> listRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "wishlist", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "productID"))
    private Set<Product> listWishList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "follow", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "artistId"))
    private Set<Users> listFollow = new HashSet<>();

}
