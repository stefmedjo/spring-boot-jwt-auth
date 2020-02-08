package app.example.store.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.example.store.role.Role;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User create(User user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User read(Long id) {
        return userRepository.getOne(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid credentials.");
        } else {

            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    authorities);
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}