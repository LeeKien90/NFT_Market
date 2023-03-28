package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.dto.request.ChangePassword;
import ra.dto.response.AuthorResponse;
import ra.dto.response.HomeUser;
import ra.model.entity.Users;
import ra.model.repository.UserRepository;
import ra.model.service.UserService;
import ra.security.CustomUserDetails;

import java.util.*;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public Users findUsersByUserName(String userName) {
        return userRepository.findUsersByUserName(userName);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users saveOrUpdate(Users users) {
        return userRepository.save(users);
    }


    @Override
    public Users findById(int userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public List<Users> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean changePass(ChangePassword changePassword, PasswordEncoder encoder) {
        Users users = userRepository.findById(changePassword.getUserId()).get();
        if (BCrypt.checkpw(changePassword.getOldPass(), users.getPasswords())) {
            users.setPasswords(encoder.encode(changePassword.getNewPass()));
            userRepository.save(users);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Users findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public List<HomeUser> getPopularArtist(int size) {
        List<Users> listUser = userRepository.getPopularArtist(size);
        List<HomeUser> list = new ArrayList<>();
        for (Users user : listUser) {
            HomeUser homeUser = new HomeUser();
            homeUser.setAuthorId(user.getUserId());
            homeUser.setAuthorName(user.getFullName());
            homeUser.setUserName(user.getUserName());
            homeUser.setAvatar(user.getAvatar());
            list.add(homeUser);
        }
        return list;
    }

    @Override
    public List<AuthorResponse> getAtistOfTheWeek(Date startDate, Date endDate, int size) {
        List<Users> listUser = userRepository.getArtistOfTheWeek(startDate, endDate, size);
        List<AuthorResponse> list = new ArrayList<>();
        for (Users users :listUser) {
            AuthorResponse authorResponse = changeData(users);
            // Lây số lượng user đang follow
            authorResponse.setFollower(userRepository.sumFollowed(users.getUserId()));
            // Lấy số luọng user đang follow
            authorResponse.setFollowing(userRepository.sumUserFollow(users.getUserId()));
            list.add(authorResponse);
        }
        return list;
    }

    private static AuthorResponse changeData(Users users) {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setUserId(users.getUserId());
        authorResponse.setAddress(users.getAddress());
        authorResponse.setFullName(users.getFullName());
        authorResponse.setAvatar(users.getAvatar());
        return authorResponse;
    }



    public List<HomeUser> getNewArtist(int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("created").descending());
        Page<Users> usersPage = userRepository.findAll(pageable);
        List<HomeUser> homeUsers = new ArrayList<>();
        for (Users users: usersPage) {
           HomeUser homeUser = new HomeUser();
            homeUser.setAuthorId(users.getUserId());
            homeUser.setAuthorName(users.getFullName());
            homeUser.setUserName(users.getUserName());
            homeUser.setAvatar(users.getAvatar());
            homeUsers.add(homeUser);
        }
        return homeUsers;

    }


    @Override
    public List<AuthorResponse> getNewComer(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("created").descending());
        Page<Users> listUser = userRepository.findAll(pageable);
        List<AuthorResponse> list = new ArrayList<>();
        for (Users users :listUser) {
            AuthorResponse authorResponse = changeData(users);
            try {
                authorResponse.setFollower(userRepository.sumFollowed(users.getUserId()));
                // Lấy số luọng user đang follow
                authorResponse.setFollowing(userRepository.sumUserFollow(users.getUserId()));
            }catch (Exception e){
                e.printStackTrace();
            }
            list.add(authorResponse);
        }
        return list;
    }

    @Override
    public List<AuthorResponse> getTopCollectors(int size) {
        List<Users> listUser = userRepository.getTopCollector(size);
        List<AuthorResponse> list = new ArrayList<>();
        for (Users users :listUser) {
            AuthorResponse authorResponse = changeData(users);
            try {
                authorResponse.setFollower(userRepository.sumFollowed(users.getUserId()));
                // Lấy số luọng user đang follow
                authorResponse.setFollowing(userRepository.sumUserFollow(users.getUserId()));
            }catch (Exception e){
                e.printStackTrace();
            }
            list.add(authorResponse);
        }
        return list;
    }

    @Override
    public Map<String, Object> searchArtictOrCollector(String name, int size, int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Users> listUser = userRepository.searchByUserNameContainingOrFullNameContaining(name,name,pageable);
        List<AuthorResponse> list = new ArrayList<>();
        for (Users users :listUser) {
            AuthorResponse authorResponse = changeData(users);
            // Lây số lượng user đang follow
            try {
                authorResponse.setFollower(userRepository.sumFollowed(users.getUserId()));
                // Lấy số luọng user đang follow
                authorResponse.setFollowing(userRepository.sumUserFollow(users.getUserId()));
            }catch (Exception e){
                e.printStackTrace();
            }
            list.add(authorResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", listUser.getSize());
        listResponse.put("totalItems", listUser.getTotalElements());
        listResponse.put("totalPage", listUser.getTotalPages());
        return listResponse;
    }

    @Override
    public boolean follow(int artistId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(customUserDetails.getUserId()).get();
        users.getListFollow().add(userRepository.findById(artistId).get());
        try {
            userRepository.save(users);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String unFollow(int artistId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(customUserDetails.getUserId()).get();

        Users artist = null;
        try {
           artist =  userRepository.findById(artistId).get();
        }catch (Exception e){
            e.printStackTrace();
            return "ID nay khong ton tai";
        }
            boolean check = false;
            for (Users user: users.getListFollow()) {
                if (artistId == user.getUserId()){
                   check =true;
                    break;
                }
            }
            if (check){
                users.getListFollow().remove(artist);
                try {
                    userRepository.save(users);
                    return "Thanh Cong";
                }catch (Exception e){
                    e.printStackTrace();
                    return "That Bai";
                }
            }else {
                return "Artist nay chua duoc follow";
            }
    }


}
