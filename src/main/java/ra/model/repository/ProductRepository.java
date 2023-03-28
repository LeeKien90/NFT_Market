package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.dto.response.ProductResponse;
import ra.model.entity.Product;
import ra.model.entity.Tag;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findAllByArtist_UserId(int id, Pageable pageable);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID\n" +
            "           from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID join bid b3 on b.productTimeId = b3.productTimeId join user u on b3.userID = u.UserId\n" +
            "            WHERE  NOW() BETWEEN b2.startime AND b2.endtime\n" +
            "            group by p.productId\n" +
            "            order by count(u.UserId) desc limit 5", nativeQuery = true)
    List<Product> findMostBidding();

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID\n" +
            "                       from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID join bid b3 on b.productTimeId = b3.productTimeId join user u on b3.userID = u.UserId\n" +
            "                    WHERE  NOW() BETWEEN b2.startime AND b2.endtime\n" +
            "            group by p.productId\n" +
            "order by count(u.UserId) desc limit :size offset :page ", nativeQuery = true)
    List<Product> getLiveAuctionProduct(@Param("size") int size, @Param("page") int page);


    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID, b.bidTimeID, b.productID, b.bidTimeId, b2.endTime, b2.starTime\n" +
            "from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID\n" +
            "WHERE (NOW() BETWEEN b2.startime AND b2.endtime) and p.userID = :uId\n" +
            "order by p.productId asc limit 3", nativeQuery = true)
    List<Product> findSortBidding(@Param("uId") int userId);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID, b.bidTimeID, b.productID, b.bidTimeId, b2.endTime, b2.starTime\n" +
            "from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID\n" +
            "WHERE (NOW() BETWEEN b2.startime AND b2.endtime) and p.userID = :uId\n", nativeQuery = true)
    List<Product> findAllBidding(@Param("uId") int userId);

    Page<Product> findProductByListTagContaining(Tag tag, Pageable pageable);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p join history h on p.productId = h.productID where p.productPrice>0", nativeQuery = true)
    List<Product> getALLProductFormHistory();

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p where p.userID=p.ownersID and p.productPrice>0", nativeQuery = true)
    List<Product> getAllOwnedByCreator();

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID where (NOW() BETWEEN b2.startime AND b2.endtime)", nativeQuery = true)
    List<Product> findAllReserverdPrice();
    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p join history h on p.productId = h.productID where p.productPrice>0", nativeQuery = true)
    Page<Product> getALLProductFormHistory(Pageable pageable);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p where p.userID=p.ownersID and p.productPrice>0", nativeQuery = true)
    Page<Product> getAllOwnedByCreator(Pageable pageable);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p join bidtimeproduct b on p.productId = b.productID join bidtime b2 on b2.bidTimeId = b.bidTimeID where (NOW() BETWEEN b2.startime AND b2.endtime)", nativeQuery = true)
    Page<Product> findAllReserverdPrice(Pageable pageable);

    Page<Product> searchByProductNameContainsOrArtist_FullNameOrOwner_UserName(String nameArtist,String nameProductName,String nameOwner, Pageable pageable);

    Page<Product> findAllByOwner_UserId(int id, Pageable pageable);

    @Query(value = "select p.productId, p.createDate, p.productImage, p.productName, p.productPrice, p.userID, p.ownersID from product p where p.productPrice>0", nativeQuery = true)
    Page<Product> getALlPro(Pageable pageable);

}
