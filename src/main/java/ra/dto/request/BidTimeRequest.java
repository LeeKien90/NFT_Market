package ra.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BidTimeRequest {
    private int bidTimeId;
    private Date startTime;
    private Date endTime;
//    private List<Integer> productId;
}
