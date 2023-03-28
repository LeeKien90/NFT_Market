package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Bid;
import ra.model.entity.Product;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid,Integer> {
    List<Bid> findByProductTime_ProductTimeIdOrderByBidMoneyDesc(int productTimeId);
    List<Bid> findAllByProductTime_Product(Product product);
}
