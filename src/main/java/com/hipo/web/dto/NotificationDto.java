package com.hipo.web.dto;


import com.hipo.domain.entity.Notification;
import com.hipo.domain.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Long id;

    private String message;

    private boolean checked;

    private NotificationType notificationType;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.checked = notification.isChecked();
        this.notificationType = notification.getNotificationType();
    }
}
