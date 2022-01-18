package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.NotificationSearchCond;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"7. Notification"})
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "알림 목록 조회", notes = "notificationType과 checked를 검색 조건으로 받을 수 있습니다.\n" +
            "page와 size를 받으면 Page로 채팅방 메시지 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 채팅방 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/notifications")
    public Object findNotifications(@ApiIgnore @LoginAccountId Long accountId,
                                    NotificationSearchCond notificationSearchCond, @ApiIgnore Pageable pageable,
                                    boolean all) {
        if (all) {
            return new Result<>(notificationService.findNotifications(accountId, notificationSearchCond, pageable, all));
        }
        return notificationService.findNotifications(accountId, notificationSearchCond, pageable, all);
    }

    @PostMapping("/notification/checked")
    public void checked() {
    }
}
