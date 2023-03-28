package ra.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tag")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "tagID")
    private int tagID;
    @JoinColumn(name = "tagName")
    private String tagName;
    @JoinColumn(name = "tagStatus")
    private boolean tagStatus;
}
