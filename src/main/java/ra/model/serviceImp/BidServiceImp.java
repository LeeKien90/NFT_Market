package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.dto.response.BidResponse;
import ra.model.entity.*;
import ra.model.repository.*;
import ra.model.service.BidService;
import ra.security.CustomUserDetails;

import java.util.Date;
import java.util.List;
@Service
public class BidServiceImp implements BidService {
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductTimeRepository productTimeRepository;
    @Autowired
    private BidTimeRepository bidTimeRepository;


    @Override
    public List<Bid> getAll() {
        return null;
    }

    @Override
    public Bid findbyId(Integer bidId) {
        return null;
    }

    @Override
    public String created(int productTimeId, int price) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(customUserDetails.getUserId()).get();
        BidTime bidTime = bidTimeRepository.checkNow();
        Bid bid = new Bid();
        if (bidTime!=null){
            bid.setBidDateTime(new Date());
            boolean check = false;
//            tim id product co trong phien dau gia
            for (ProductTime productTime : bidTime.getListProduct()) {
                if (productTimeId == productTime.getProductTimeId()){
                    check = true;
                    break;
                }
            }
            if (check){
                ProductTime productTime = productTimeRepository.findById(productTimeId).get();
                bid.setProductTime(productTime);
                bid.setUsers(users);
//                gia dau gia phai lon hon gia cao nhat
                List<Bid> listBid = bidRepository.findByProductTime_ProductTimeIdOrderByBidMoneyDesc(productTimeId);
                if (listBid.size()==0){
                    if (price>productTime.getStartPrice()){
                        bid.setBidMoney(price);
                    }else {
                        return "gia dat cuoc phai lon hon gia khoi diem, "+ "gia khoi diem la:" + productTime.getStartPrice();
                    }
                }else {
                    if (price > listBid.get(0).getBidMoney()){
                        bid.setBidMoney(price);
                    }else {
                        return "gia dat cuoc phai lon hon gia dat cuoc hien tai, "+"gia dat cuoc hien tai la:"+ listBid.get(0).getBidMoney();
                    }
                }

                try {
                    bidRepository.save(bid);
                    return "Ok";
                }catch (Exception e){
                    e.printStackTrace();
                    return "Error";
                }
            }else {
                return "san pham khong trong phien dau gia";
            }
        }else {
            return "Khong co phien dau gia nao tai thoi diem hien tai";
        }


    }
}
