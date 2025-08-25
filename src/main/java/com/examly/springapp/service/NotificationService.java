package com.examly.springapp.service;

import com.examly.springapp.model.Notification;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;
    public NotificationService(NotificationRepository repo){ this.repo = repo; }

    public void notify(User user, String message){
        repo.save(new Notification(user, message));
    }

    public List<Notification> unread(User user){ return repo.findByUserAndReadFlagFalse(user); }
}
