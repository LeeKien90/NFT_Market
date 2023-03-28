package ra.model.service;

import ra.dto.response.BidResponse;
import ra.model.entity.Bid;
import ra.model.entity.ProductTime;

import java.util.List;

public interface BidService {
    List<Bid> getAll();
    Bid findbyId(Integer bidId);
    String created(int productTimeId,int price);


}
