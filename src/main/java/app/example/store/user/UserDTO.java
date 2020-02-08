package app.example.store.user;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String password;
    private String fname;
    private String lname;
    private boolean isAccountNonExpired;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

}