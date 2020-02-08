package app.example.store.user;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.example.store.jwt.JwtCanceledAccessToken;
import app.example.store.jwt.JwtCanceledAccessTokenService;
import app.example.store.jwt.JwtTokens;
import app.example.store.jwt.JwtUtil;
import app.example.store.role.Role;
import app.example.store.role.RoleService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtCanceledAccessTokenService jwtCanceledAccessTokenService;

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public ResponseEntity<?> hello(){

        return ResponseEntity.status(HttpStatus.OK).body("Hello world");
    }

    @RequestMapping(path = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        if(Strings.isNullOrEmpty(request.getPassword()) || Strings.isNullOrEmpty(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
            JwtTokens tokens = jwtUtil.generateTokens(userDetails);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        }
    }

    @RequestMapping(path = "/auth/register", method=RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserDTO req){
        if(Strings.isNullOrEmpty(req.getFname()) || 
            Strings.isNullOrEmpty(req.getLname()) || 
            Strings.isNullOrEmpty(req.getUsername()) || 
            Strings.isNullOrEmpty(req.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            if(userService.findUserByUsername(req.getUsername()) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated username.");
            }else{
                List<Role> roles = new ArrayList<>();
                roles.add(roleService.list().get(0));
                User user = new User(null, req.getUsername(), req.getPassword(), true, true, true, roles, req.getFname(), req.getLname());
                user = userService.create(user);
                UserDetails userDetails = userService.loadUserByUsername(req.getUsername());
                JwtTokens tokens = jwtUtil.generateTokens(userDetails);
                return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
            }
        }
    }

    @RequestMapping(path = "/auth/refresh", method=RequestMethod.POST)
    public ResponseEntity<?> refresh(@RequestBody JwtTokens req){
        if(Strings.isNullOrEmpty(req.getAccessToken()) || Strings.isNullOrEmpty(req.getRefreshToken())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            String username = jwtUtil.getUsernameFromToken(req.getAccessToken());
            UserDetails userDetails = userService.loadUserByUsername(username);
            if(jwtUtil.canBeRefreshed(req.getAccessToken(), req.getRefreshToken(),userDetails)){
                JwtTokens tokens = jwtUtil.generateTokens(userDetails);
                return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
            }else{
                /**
                 * Tokens are not valid and can't be refreshed
                 * so we cancel them.
                 */
                JwtCanceledAccessToken token = new JwtCanceledAccessToken(req.getAccessToken());
                jwtCanceledAccessTokenService.create(token);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @RequestMapping(path="/users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        if(Strings.isNullOrEmpty(userDTO.getFname()) || Strings.isNullOrEmpty(userDTO.getLname())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            User user = userService.read(id);
            if(user == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
            }else{
                user.setAccountNonExpired(userDTO.isAccountNonExpired());
                user.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
                user.setEnabled(userDTO.isEnabled());
                user.setFname(userDTO.getFname());
                user.setLname(userDTO.getLname());
                user.setUsername(userDTO.getUsername());
                user = userService.update(user);
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
        }
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public ResponseEntity<?> list(){
        List<User> users = userService.list();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


}