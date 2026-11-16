package com.hireflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_Id(Long userId);

    List<Notification> findByUser_IdAndReadFalse(Long userId);

    Optional<Notification> findByIdAndUser_Id(Long id, Long userId);
}