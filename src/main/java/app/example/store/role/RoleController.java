package app.example.store.role;

import java.util.List;

import com.google.common.base.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    RoleService roleService;
    

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Role role){
        if(Strings.isNullOrEmpty(role.getName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            role = roleService.create(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(role);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        Role role = roleService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Role roleDTO){
        if(Strings.isNullOrEmpty(roleDTO.getName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            Role role = roleService.read(id);
            if(role == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
            }else{
                role.setName(roleDTO.getName());
                role = roleService.update(role);
                return ResponseEntity.status(HttpStatus.OK).body(role);
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Role role = roleService.read(id);
        if(role == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
        }else{
            roleService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        }
    }

    @GetMapping
    public ResponseEntity<?> list(){
        List<Role> roles = roleService.list();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

}