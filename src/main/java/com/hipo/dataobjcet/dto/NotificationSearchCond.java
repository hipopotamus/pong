package com.hipo.dataobjcet.dto;

import com.hipo.domain.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSearchCond {

    private NotificationType notificationType;

    private boolean checked;
}
