package com.hipo.service;

import com.hipo.dataobjcet.dto.IdDto;
import com.hipo.dataobjcet.dto.NotificationDto;
import com.hipo.dataobjcet.dto.NotificationSearchCond;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Notification;
import com.hipo.domain.entity.enums.NotificationType;
import com.hipo.exception.AuthException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.NotificationRepository;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void createNotification(String message, NotificationType notificationType, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        notificationRepository.save(new Notification(message, notificationType, account));
    }

    public Iterable<NotificationDto> findNotifications(Long accountId, NotificationSearchCond notificationSearchCond,
                                                       Pageable pageable, boolean all) {
        if (all) {
            return notificationRepository.findAllNotification(accountId, notificationSearchCond).stream()
                    .map(NotificationDto::new)
                    .collect(Collectors.toList());
        }

        QueryResults<Notification> notifications = notificationRepository.findNotifications(accountId, notificationSearchCond, pageable);

        List<NotificationDto> notificationDtoList = notifications.getResults().stream()
                .map(NotificationDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(notificationDtoList, pageable, notifications.getTotal());
    }

    @Transactional
    public void check(Long accountId, List<IdDto> notificationIdList) {

        for (IdDto notificationId : notificationIdList) {
            Notification notification = notificationRepository.findNotificationWithAccount(notificationId.getId())
                    .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Notification을 찾을 수 없습니다."));

            if (!notification.getAccount().getId().equals(accountId)) {
                throw new AuthException("내 알람이 아닙니다.");
            }

            notification.check();
        }
    }
}
