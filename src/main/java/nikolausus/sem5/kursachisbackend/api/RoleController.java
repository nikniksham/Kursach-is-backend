package nikolausus.sem5.kursachisbackend.api;

import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping
    public Optional<Role> getRoleById(@RequestParam Long id) {
        return roleService.getRoleById(id);
    }
}
