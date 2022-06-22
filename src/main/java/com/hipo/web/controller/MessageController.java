package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.domain.entity.Message;
import com.hipo.service.MessageService;
import com.hipo.validator.MessageValidator;
import com.hipo.web.dto.MessageDto;
import com.hipo.web.dto.Result;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageValidator messageValidator;

    @GetMapping("/message/{chatRoomId}")
    public Slice<MessageDto> getChatRoomMessageBySlice(@LoginAccountId Long loginAccountId,
                                                       @PathVariable Long chatRoomId, Pageable pageable) {
        messageValidator.isChatRoomMember(loginAccountId, chatRoomId);

        QueryResults<Message> chatRoomMessageQueryResults =
                messageService.findChatRoomMessageBySlice(loginAccountId, chatRoomId, pageable);

        List<MessageDto> messageDtoList = chatRoomMessageQueryResults.getResults().stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());

        if (messageDtoList.size() == pageable.getPageSize() + 1) {
            return new SliceImpl<>(messageDtoList.subList(0, pageable.getPageSize()), pageable, true);
        } else {
            return new SliceImpl<>(messageDtoList, pageable, false);
        }
    }

    @GetMapping("message/all/{chatRoomId}")
    public Result<List<MessageDto>> getAllChatRoomMessage(@LoginAccountId Long loginAccountId,
                                                          @PathVariable Long chatRoomId) {
        messageValidator.isChatRoomMember(loginAccountId, chatRoomId);

        List<MessageDto> messageDtoList = messageService.findAllChatRoomMessage(loginAccountId, chatRoomId).stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());

        return new Result<>(messageDtoList);
    }
}
