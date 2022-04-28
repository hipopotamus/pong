package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.NotificationIdListDto;
import com.hipo.dataobjcet.dto.NotificationSearchCond;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public Object findNotifications(@LoginAccountId Long accountId,
                                    NotificationSearchCond notificationSearchCond, Pageable pageable,
                                    boolean all) {
        if (all) {
            return new Result<>(notificationService.findNotifications(accountId, notificationSearchCond, pageable, all));
        }
        return notificationService.findNotifications(accountId, notificationSearchCond, pageable, all);
    }

    @PostMapping("/notification/check")
    public ResultMessage checked(@LoginAccountId Long accountId,
                                 @RequestBody NotificationIdListDto notificationIdListDto) {
        notificationService.check(accountId, notificationIdListDto.getNotificationIdList());

        return new ResultMessage("success check notificationList");
    }
}
