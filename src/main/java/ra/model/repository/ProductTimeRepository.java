package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.ProductTime;

import java.util.List;

@Repository
public interface ProductTimeRepository extends JpaRepository<ProductTime,Integer> {
    ProductTime findByProduct_ProductIdAndBidTime_BidTimeId(int productId,int bidTimeId);
    List<ProductTime> findAllByBidTime_BidTimeId(int id);
}
