package ra.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductResponse {
    private int productId;
    private String productName;
    private String productImage;
    private float price;
    private int artistId;
    private String artistName;
    private String ownerName;
    private int ownerId;
   private  List<TagResponse> listTag = new ArrayList<>();
    private List<BidResponse> listBid = new ArrayList<>();
    private List<HistoryResponse> listHistory = new ArrayList<>();
    private Date endTime;
}
