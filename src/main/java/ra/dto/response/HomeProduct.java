package ra.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class HomeProduct {
    private int productId;
    private String productName;
    private float curentBid;
    private String authorName;
    private int authorId;
    private Date endtime;
    private float reservePrice;
}
