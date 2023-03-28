package ra.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "bidTime")
public class BidTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "bidTimeID")
    private int bidTimeId;
    @JoinColumn(name = "starTime")
    private Date starTime;
    @JoinColumn(name = "endTime")
    private Date endTime;
    @OneToMany(mappedBy = "bidTime")
    private List<ProductTime> listProduct = new ArrayList<>();
}
