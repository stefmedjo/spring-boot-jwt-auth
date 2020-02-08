package app.example.store.role;

import java.util.List;

public interface RoleService {

    Role create(Role role);
    Role read(Long id);
    Role update(Role role);
    void delete(Long id);
    List<Role> list();
    
}