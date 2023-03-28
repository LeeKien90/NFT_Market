package ra.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Table(name = "exhibition")
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name="exhibitionID")
    private int exhibitionId;
    @JoinColumn(name = "exhibitionTitle")
    private String exhibitionTitle;
    @JoinColumn(name = "exhibitionDescription")
    private String exhibitionDescription;
    @JoinColumn(name = "exhibitionCreatedDate")
    private Date startTime;
    @JoinColumn(name = "exhibitionExpiredDate")
    private Date endTime;
    @JoinColumn(name = "image")
    private String image;
    @JoinColumn(name = "exhibitionStatus")
    private boolean exhibitionStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "productexhibition", joinColumns = @JoinColumn(name = "exhibitionID"), inverseJoinColumns = @JoinColumn(name = "productID"))
    private List<Product> listProduct = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "exhibitiontag", joinColumns = @JoinColumn(name = "exhibitionId"), inverseJoinColumns = @JoinColumn(name = "tagID"))
    private Set<Tag> listTag = new HashSet<>();
}
