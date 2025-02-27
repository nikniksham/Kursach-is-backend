package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.CommentOnApplicationsDTO;
import nikolausus.sem5.kursachisbackend.entity.CommentOnApplications;

public class CommentOnApplicationsMapper {
    public static CommentOnApplicationsDTO toDTO(CommentOnApplications commentOnApplications) {
        return new CommentOnApplicationsDTO(commentOnApplications.getId(),
                commentOnApplications.getText(),
                ApplicationsMapper.toDTO(commentOnApplications.getApplications()),
                UserMapper.toDTO(commentOnApplications.getUser())
        );
    }

    public static CommentOnApplications toEntity(CommentOnApplicationsDTO commentOnApplicationsDTO) {
        CommentOnApplications commentOnApplications = new CommentOnApplications();
        commentOnApplications.setId(commentOnApplicationsDTO.getId());
        commentOnApplications.setText(commentOnApplicationsDTO.getText());
        commentOnApplications.setApplications(ApplicationsMapper.toEntity(commentOnApplicationsDTO.getApplicationsDTO()));
        commentOnApplications.setUser(UserMapper.toEntity(commentOnApplicationsDTO.getUserDTO()));
        return commentOnApplications;
    }
}
