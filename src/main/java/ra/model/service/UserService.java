package ra.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import ra.dto.request.ChangePassword;
import ra.dto.response.AuthorResponse;
import ra.dto.response.HomeUser;
import ra.model.entity.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {
    Users findUsersByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users saveOrUpdate(Users users);
    Users findById(int userId);
    List<Users> getAll();
    boolean changePass(ChangePassword changePassword, PasswordEncoder encoder);
    Users findByEmail(String userEmail);
    List<HomeUser> getPopularArtist(int size);
    List<AuthorResponse> getAtistOfTheWeek(Date startDate, Date endDate,int size);

    List<HomeUser> getNewArtist(int size);
    List<AuthorResponse> getNewComer(int size);
    List<AuthorResponse> getTopCollectors(int size);

    Map<String, Object> searchArtictOrCollector(String name, int size, int page);

    boolean follow(int artistId);

    String unFollow(int artistId);
}
