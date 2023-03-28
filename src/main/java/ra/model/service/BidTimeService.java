package ra.model.service;


import ra.dto.request.BidTimeRequest;
import ra.model.entity.BidTime;

public interface BidTimeService {
    String createBidTime(BidTimeRequest bidTimeRequest);
}
