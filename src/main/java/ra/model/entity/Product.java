package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @JoinColumn(name = "productID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @JoinColumn(name = "productName")
    private String productName;
    @JoinColumn(name = "productPrice")
    private float productPrice;
    @JoinColumn(name = "productImage")
    private String productImage;
    @JoinColumn(name = "createDate")
    private Date createDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownersID")
    @JsonIgnore
    private Users owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID")
    @JsonIgnore
    private Users artist;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "producttag", joinColumns = @JoinColumn(name = "productID"), inverseJoinColumns = @JoinColumn(name = "tagID"))
    private Set<Tag> listTag = new HashSet<>();
    @OneToMany(mappedBy = "product")
    private List<History> listHistory = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<ProductTime> listProductTime = new ArrayList<>();
}
