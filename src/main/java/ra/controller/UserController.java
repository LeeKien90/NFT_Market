package ra.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ChangePassword;
import ra.dto.request.LoginRequest;
import ra.dto.request.RegisterRequest;
import ra.dto.response.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.*;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.model.serviceImp.UserServiceImp;
import ra.security.CustomUserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserService userService;
    private PasswordEncoder encoder;
    private RoleService roleService;


    //    ------------------    ĐĂNG KÝ   ----------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) throws Throwable {
        if (userService.existsByUserName(registerRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already"));
        }
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        Users user = new Users();
        user.setUserName(registerRequest.getUserName());
        user.setPasswords(encoder.encode(registerRequest.getPasswords()));
        user.setAvatar(registerRequest.getAvatar());
        user.setEmail(registerRequest.getEmail());
        user.setCreated(LocalDate.now());
        user.setStatusUser(true);
        user.setFullName(registerRequest.getFullName());
        user.setAddress(registerRequest.getAddress());
        Set<String> strRoles = registerRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            //User quyen mac dinh
            Roles userRole = (Roles) roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = null;
                        try {
                            adminRole = (Roles) roleService.findByRoleName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = null;
                        try {
                            modRole = (Roles) roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = null;
                        try {
                            userRole = (Roles) roleService.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        try {
            userServiceImp.saveOrUpdate(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping()
    public List<Users> getAll() {
        return userServiceImp.getAll();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> loginUser(@RequestBody LoginRequest userLogin) {
        Users users = userService.findUsersByUserName(userLogin.getUserName());
        if (users.isStatusUser()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPasswords())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            JwtResponse response = new JwtResponse(customUserDetail.getUserId(), jwt, customUserDetail.getUsername(), customUserDetail.getEmail(), customUserDetail.getAvatar(), listRoles);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Welcome " + userLogin.getUserName(), response));
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseObject("ok", "Tài khoản " + userLogin.getUserName() + " của bạn đã bị khóa ! Vui lòng liên hệ với admin ", ""));
        }
    }

    @GetMapping("/logOut")
    public ResponseEntity<?> logOut() {
        // Clear the authentication from server-side (in this case, Spring Security)
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("changePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody ChangePassword changePassword) {
//        USER dang dang nhap
        CustomUserDetails usersChangePass = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        changePassword.setUserId(usersChangePass.getUserId());
        boolean check = userService.changePass(changePassword, encoder);
        if (check) {

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đã cập nhật thành công tài khoản  " + usersChangePass.getUsername(), ""));
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseObject("false", "Không cập nhật được tài khoản  " + usersChangePass.getUsername(), ""));
        }
    }


    /*
    Get the top popular Artist
    input:  Param int size
    Output: List Artist
   */
    @GetMapping("getPopularArtist")
    public ResponseEntity<?> getPopularArtist(@RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(userService.getPopularArtist(size));
    }


    /*
      Get the top Artists with the best selling artworks of the week
      input:  Param int size
      Output: List Artist
   */
    @GetMapping("getAtistOfTheWeek")
    public ResponseEntity<?> getAtistOfTheWeek(@RequestParam(defaultValue = "9") int size,
                                               @RequestParam("startTime") String startTimeStr,
                                               @RequestParam("endTime") String endTimeStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date startTime = formatter.parse(startTimeStr);
        Date endTime = formatter.parse(endTimeStr);
        return ResponseEntity.ok(userService.getAtistOfTheWeek(startTime, endTime, size));
    }


    @GetMapping("/getNewArtist")
    public List<HomeUser> getNewArtist(@RequestParam(defaultValue = "9") int size) {
        return userService.getNewArtist(size);
    }

    /*
     Get new commer
     input:  Param int size
     Output: List Comer
  */
    @GetMapping("/getNewComer")
    public ResponseEntity<?> getNewComer(@RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(userService.getNewComer(size));
    }

    /*
       Get top collector: Get the top 6 collectors who own the most artworks
       input:  Param int size
       Output: List Collector
    */
    @GetMapping("/getTopCollectors")
    public ResponseEntity<?> getTopCollectors(@RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(userService.getTopCollectors(size));
    }

    /*
        Search Artist or collector by name
        input: Param String name
               Param int size
               Param int page
        Output: List Artist or Collector
     */

    @GetMapping("/searchArtistOrCollector")
    public ResponseEntity<?> searchArtistOrCollector(@RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam("name") String name) {
        return ResponseEntity.ok(userService.searchArtictOrCollector(name, size, page));
    }


    @PostMapping("/follow/{authorId}")
    public ResponseEntity<?> follow(@PathVariable("authorId") int authorId){
        boolean check = userService.follow(authorId);
        if (check){
            return ResponseEntity.ok("Follow thành công");
        }else {
            return ResponseEntity.ok("Follow thất bại");
        }
    }



    @GetMapping("/artist/{userId}")
    public ResponseEntity<?> findById(@PathVariable("userId") int userId){
          userService.findById(userId);
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PutMapping("/unFollow/{artistId}")
    public String unFollow(@PathVariable("artistId") int artistId){
       return userService.unFollow(artistId);
    }

}