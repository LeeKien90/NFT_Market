package ra.dto.response;

import lombok.Data;

import java.util.Date;
@Data
public class BidResponse {
    private int bidId;
    private int userId;
    private String userName;
    private float price;
    private Date createdDate;
}
