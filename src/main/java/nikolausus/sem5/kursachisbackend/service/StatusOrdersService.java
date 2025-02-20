package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.StatusOrders;
import nikolausus.sem5.kursachisbackend.repository.StatusOrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusOrdersService {
    private final StatusOrdersRepository statusOrdersRepository;

    public StatusOrdersService(StatusOrdersRepository statusOrdersRepository) {
        this.statusOrdersRepository = statusOrdersRepository;
    }

    public List<StatusOrders> getAllStatusOrders() {
        return statusOrdersRepository.findAll();
    }

    public Optional<StatusOrders> getStatusOrdersById(Long id) {
        return statusOrdersRepository.findById(id);
    }
}
