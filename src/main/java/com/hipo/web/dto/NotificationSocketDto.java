package com.hipo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSocketDto {

    private NotificationDto notificationDto;

    private NotificationCheckDto notificationCheckDto;
}
