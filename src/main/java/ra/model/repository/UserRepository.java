package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Product;
import ra.model.entity.Users;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findUsersByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Page<Users> findByUserNameContaining(String name, Pageable pageable);
    Users findByEmail(String email);

    @Query(value = "select u.UserId, u.userAvatar, u.userEmail, u.userPassword, u.userStatus, u.UserName, u.fullName,u.address,u.created\n" +
            "from user u join follow f on u.UserId = f.artistId\n" +
            "group by f.artistId\n" +
            "order by count(f.userId) desc limit :size",nativeQuery = true)
    List<Users> getPopularArtist(@Param("size")int size);

    @Query(value = "select u.UserId, u.userAvatar, u.userEmail, u.userPassword, u.userStatus, u.UserName, u.fullName,u.address,u.created\n" +
            "from user u join product p on u.UserId = p.userID join history h on p.productId = h.productID\n" +
            "where h.historyTime <= :endTime and h.historyTime >= :startTime\n" +
            "group by p.userID\n" +
            "order by count(h.historyId) desc  limit :size",nativeQuery = true)
    List<Users> getArtistOfTheWeek(@Param("startTime") Date startTime,@Param("endTime")Date endTime,@Param("size")int size);
    @Query(value = "select  count(f.userId)\n" +
            "            from user u join follow f on u.UserId = f.artistId\n" +
            "            where u.UserId = :uId\n" +
            "           group by f.artistId",nativeQuery = true)
    int sumFollowed(@Param("uId") int userId);
    @Query(value = "select  count(f.artistId)\n" +
            "            from user u join follow f on u.UserId = f.userId\n" +
            "            where u.UserId = :uId\n" +
            "           group by f.userId",nativeQuery = true)
    int sumUserFollow(@Param("uId") int userId);
    @Query(value = "select u.UserId, userAvatar, userEmail, userPassword, userStatus, UserName, fullName, address,u.created\n" +
            "from user u join product p on u.UserId = p.ownersID\n" +
            "group by u.UserId\n" +
            "order by count(p.ownersID) desc limit :size",nativeQuery = true)
    List<Users> getTopCollector(@Param("size")int size);

    Page<Users> searchByUserNameContainingOrFullNameContaining(String name,String fName,Pageable pageable);



}
