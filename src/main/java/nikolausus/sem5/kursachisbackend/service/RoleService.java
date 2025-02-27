package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.RoleDTO;
import nikolausus.sem5.kursachisbackend.Mapper.RoleMapper;
import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.repository.RoleRepository;
import org.springframework.stereotype.Service;


@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDTO getRoleById(Long id) {
        return RoleMapper.toDTO(roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Роль с заданным id не найдена")));
    }

    public RoleDTO getRoleByName(String name) {
        return RoleMapper.toDTO(roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Не смог найти роль " + name)));
    }
}
