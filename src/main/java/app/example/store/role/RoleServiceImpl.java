package app.example.store.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> list() {
        return roleRepository.findAll();
    }

    @Override
    public Role read(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    
}