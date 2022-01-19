package com.hipo.repository;

import com.hipo.domain.entity.Notification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    @EntityGraph(attributePaths = {"account"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select notification from Notification notification where notification.id = :notificationId")
    Optional<Notification> findNotificationWithAccount(@Param("notificationId") Long notificationId);

}
