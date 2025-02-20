package nikolausus.sem5.kursachisbackend.service;

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

    public Optional<CommentOnApplications> getCommentByApplicationId(Long application_id) {
        return commentOnApplicationsRepository.getCommentOnApplicationsByApplications(applicationsRepository.getApplicationsById(application_id).orElseThrow());
    }

    public CommentOnApplications createCommentOnApplication(Long application_id, User user, String text) {
        CommentOnApplications commentOnApplications = new CommentOnApplications();
        commentOnApplications.setApplications(applicationsRepository.getApplicationsById(application_id).orElseThrow());
        commentOnApplications.setUser(user);
        commentOnApplications.setText(text);
        return commentOnApplicationsRepository.save(commentOnApplications);
    }

    public CommentOnApplications editCommentOnApplication(String text, Long comment_id) {
        CommentOnApplications commentOnApplications = commentOnApplicationsRepository.getCommentOnApplicationsById(comment_id).orElseThrow();
        commentOnApplications.setText(text);
        return commentOnApplicationsRepository.save(commentOnApplications);
    }
}
