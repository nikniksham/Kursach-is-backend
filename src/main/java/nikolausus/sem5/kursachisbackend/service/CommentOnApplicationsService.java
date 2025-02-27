package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.ApplicationsDTO;
import nikolausus.sem5.kursachisbackend.DTO.CommentOnApplicationsDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.ApplicationsMapper;
import nikolausus.sem5.kursachisbackend.Mapper.CommentOnApplicationsMapper;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.entity.CommentOnApplications;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import nikolausus.sem5.kursachisbackend.repository.CommentOnApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentOnApplicationsService {
    private final CommentOnApplicationsRepository commentOnApplicationsRepository;
    private final ApplicationsRepository applicationsRepository;

    public CommentOnApplicationsService(CommentOnApplicationsRepository commentOnApplicationsRepository, ApplicationsRepository applicationsRepository) {
        this.commentOnApplicationsRepository = commentOnApplicationsRepository;
        this.applicationsRepository = applicationsRepository;
    }

    public CommentOnApplicationsDTO getCommentByApplication(ApplicationsDTO applicationsDTO) {
        return CommentOnApplicationsMapper.toDTO(
                commentOnApplicationsRepository.getCommentOnApplicationsByApplications(ApplicationsMapper.toEntity(applicationsDTO))
                        .orElseThrow(()->new RuntimeException("Комментарий не найден"))
        );
    }

    public CommentOnApplications createCommentOnApplication(Long application_id, UserDTO userDTO, String text) {
        CommentOnApplications commentOnApplications = new CommentOnApplications();
        commentOnApplications.setApplications(applicationsRepository.getApplicationsById(application_id).orElseThrow());
        commentOnApplications.setUser(UserMapper.toEntity(userDTO));
        commentOnApplications.setText(text);
        return commentOnApplicationsRepository.save(commentOnApplications);
    }

    public CommentOnApplications editCommentOnApplication(String text, Long comment_id) {
        CommentOnApplications commentOnApplications = commentOnApplicationsRepository.getCommentOnApplicationsById(comment_id).orElseThrow();
        commentOnApplications.setText(text);
        return commentOnApplicationsRepository.save(commentOnApplications);
    }
}
