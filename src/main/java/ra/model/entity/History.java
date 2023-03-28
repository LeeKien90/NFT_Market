package ra.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "historyID")
    private int historyId;
    @JoinColumn(name = "historyDateTime")
    private Date historyTime;
    @JoinColumn(name = "historyPrice")
    private float historyPrice;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productID")
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID")
    private Users users;
}
