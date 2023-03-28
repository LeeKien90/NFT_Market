package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.BidTimeRequest;
import ra.model.entity.BidTime;
import ra.model.repository.BidTimeRepository;
import ra.model.service.BidTimeService;


import java.util.Calendar;
import java.util.List;

@Service
public class BidTimeServiceImp implements BidTimeService {
    @Autowired
    private BidTimeRepository bidTimeRepository;


    @Override
    public String createBidTime(BidTimeRequest bidTimeRequest) {
        BidTime bidTime = new BidTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
//        kiểm tra thời gian hiện tại có phiên đấu giá nào không
        if ( calendar.getTime().getTime() <=  bidTimeRequest.getStartTime().getTime()){
                List<BidTime> listBidTime = bidTimeRepository.listEndTime();
                boolean check = true;
//                kiểm tra thời gian minhf nhập vào có nằm trong list bitTime không.
            for (BidTime bidTime1:listBidTime) {
                if ((bidTime1.getStarTime().getTime()<= bidTimeRequest.getStartTime().getTime() && bidTime1.getEndTime().getTime() >= bidTimeRequest.getStartTime().getTime())
                    || (bidTime1.getStarTime().getTime()<=bidTimeRequest.getEndTime().getTime() && bidTime1.getEndTime().getTime() >= bidTimeRequest.getEndTime().getTime())){
                    check = false;
                    break;
                }
            }
//            Nếu check là true thì taạo bitTime, nếu là false thì Date mình nhập vào nằm trong listBitTime
            if (check){
                bidTime.setStarTime(bidTimeRequest.getStartTime());
                bidTime.setEndTime(bidTimeRequest.getEndTime());
                try {
                    bidTimeRepository.save(bidTime);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return "Đã tạo phiên đấu giá thành công";
            }else {
                return "Thời gian không phù hợp. Đã có phiên đấu giá trong thời gian đấy";
            }
        }else {
            return "ngày bắt đầu đấu giá phải lớn hơn ngày hiện tại ";
        }

    }

}
