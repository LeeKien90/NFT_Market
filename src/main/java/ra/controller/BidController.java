package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.BidTimeRequest;
import ra.dto.response.BidResponse;
import ra.model.entity.BidTime;
import ra.model.service.BidService;
import ra.model.service.BidTimeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/bid")
public class BidController {

    @Autowired
    private BidService bidService;
    @Autowired
    private BidTimeService bidTimeService;

    @PostMapping("/createBid")
    public ResponseEntity<?> createBid(@RequestParam("productTimeId")int productTimeId,@RequestParam("price")int price){
        return ResponseEntity.ok(bidService.created(productTimeId,price));
    }

    @PostMapping("/createBidTime")
    public ResponseEntity<?> createBidTime(@RequestBody BidTimeRequest bidTimeRequest){
        return ResponseEntity.ok(bidTimeService.createBidTime(bidTimeRequest));
    }
}
