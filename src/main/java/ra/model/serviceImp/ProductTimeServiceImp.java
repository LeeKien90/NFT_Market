package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.ProductTimeRequest;
import ra.model.entity.BidTime;
import ra.model.entity.Product;
import ra.model.entity.ProductTime;
import ra.model.repository.BidTimeRepository;
import ra.model.repository.ProductRepository;
import ra.model.repository.ProductTimeRepository;
import ra.model.service.ProductTimeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProductTimeServiceImp implements ProductTimeService {
    @Autowired
    private ProductTimeRepository productTimeRepository;
    @Autowired
    private BidTimeRepository bidTimeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public String createProductTime(ProductTimeRequest productTimeRequest) {
        ProductTime productTime = new ProductTime();
        BidTime bidTime = bidTimeRepository.findById(productTimeRequest.getBidTimeId()).get();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 6);
        if (calendar.getTime().getTime()< bidTime.getStarTime().getTime()){
            List<ProductTime> listProductTime = productTimeRepository.findAllByBidTime_BidTimeId(productTimeRequest.getBidTimeId());
            boolean check = true;
            for (ProductTime pTime: listProductTime) {
                if (pTime.getProduct().getProductId() == productTimeRequest.getProductId()){
                    check = false;
                    break;
                }
            }
            if (check){
                productTime.setProduct(productRepository.findById(productTimeRequest.getProductId()).get());
                productTime.setBidTime(bidTimeRepository.findById(productTimeRequest.getBidTimeId()).get());
                productTime.setStartPrice(productTimeRequest.getStartPrice());
                try {
                    productTimeRepository.save(productTime);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return "Add sản phẩm vào phiên đấu giá thành công";
            }else {
                return "Sản phẩm đã có trong phiên đấu giá";
            }
        }else {
            return "Cần thêm sản phẩm trước phiên đấu giá trước 6 tiếng";
        }

    }
}
