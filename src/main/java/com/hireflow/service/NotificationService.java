package com.hireflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hireflow.entity.Notification;
import com.hireflow.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUser_IdAndReadFalse(userId);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {

        Notification notification = notificationRepository
                .findByIdAndUser_Id(notificationId, userId)
                .orElseThrow(() ->
                        new IllegalStateException("Notification not found"));

        notification.setRead(true);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}