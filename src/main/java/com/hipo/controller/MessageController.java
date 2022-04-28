package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/message/{chatRoomId}")
    public Object findChatRoomMessage(@ApiIgnore @LoginAccountId Long loginAccountId,
                                      @PathVariable("chatRoomId") Long chatRoomId, Pageable pageable,
                                      @RequestParam(value = "paged", required = false) boolean all) {
        if (all) {
            return new Result<>(messageService.findChatRoomMessage(loginAccountId, chatRoomId, pageable, all));
        }
        return messageService.findChatRoomMessage(loginAccountId, chatRoomId, pageable, all);
    }

}
