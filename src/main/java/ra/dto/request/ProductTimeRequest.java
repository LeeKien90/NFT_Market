package ra.dto.request;

import lombok.Data;
import ra.model.entity.BidTime;

import java.util.List;

@Data
public class ProductTimeRequest {
    private int productTimeId;
    private float startPrice;
    private int bidTimeId;
    private int productId;
}
