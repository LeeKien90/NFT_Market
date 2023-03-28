package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.entity.BidTime;
import ra.model.entity.Product;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BidTimeRepository extends JpaRepository<BidTime,Integer> {
//    BidTime findByStarTimeLessThanEqualAndEndTimeGreaterThanEqualOOrderByStarTime(LocalDateTime now1, LocalDateTime now2);
    @Query(value = "SELECT b.bidTimeId, b.endTime, b.starTime FROM bidtime b\n" +
            "WHERE NOW() BETWEEN b.startime AND b.endtime",nativeQuery = true)
    BidTime checkNow();

    @Query(value = "select b.bidTimeId,b.starTime,b.endTime\n" +
            "from bidtime b\n" +
            "where NOW() < b.endTime",nativeQuery = true)
    List<BidTime> listEndTime();



}
