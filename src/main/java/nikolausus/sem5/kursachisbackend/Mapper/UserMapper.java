package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.entity.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getRoles().stream().map(RoleMapper::toDTO).collect(Collectors.toSet())
        );
    }

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRolesDTO().stream().map(RoleMapper::toEntity).collect(Collectors.toSet()));
        return user;
    }
}
