package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.service.ChatRoomService;
import com.hipo.web.dto.ChatRoomDto;
import com.hipo.web.dto.Result;
import com.hipo.web.dto.ResultMessage;
import com.hipo.web.form.ChatRoomMasterForm;
import com.hipo.web.form.ChatRoomNameForm;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chatRoom")
    public ResultMessage createChatRoom(@LoginAccountId Long loginAccountId,
                                        @Valid @RequestBody ChatRoomNameForm chatRoomNameForm) {

        chatRoomService.createChatRoom(loginAccountId, chatRoomNameForm.getName());

        return new ResultMessage("success create ChatRoom");
    }

    @PostMapping("/chatRoom/name")
    public ResultMessage updateChatRoomName(@LoginAccountId Long loginAccountId,
                                            @Valid @RequestBody ChatRoomNameForm chatRoomNameForm) {

        chatRoomService.updateChatRoomName(loginAccountId, chatRoomNameForm.getChatRoomId(), chatRoomNameForm.getName());

        return new ResultMessage("success update ChatRoomName");
    }

    @PostMapping("/chatRoom/master")
    public ResultMessage updateChatRoomMaster(@LoginAccountId Long loginAccountId,
                                              @Valid @RequestBody ChatRoomMasterForm chatRoomMasterForm) {
        chatRoomService.updateChatRoomMasterAccount(loginAccountId, chatRoomMasterForm.getChatRoomId(), chatRoomMasterForm.getAccountId());

        return new ResultMessage("success update ChatRoomName");
    }

    @GetMapping("/chatRooms")
    public Page<ChatRoomDto> getChatRoomByPage(@LoginAccountId Long loginAccountId, Pageable pageable) {
        QueryResults<ChatRoom> chatRoomQueryResults = chatRoomService.findChatRoomByPage(loginAccountId, pageable);
        List<ChatRoomDto> chatRoomDtoList = chatRoomQueryResults.getResults().stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(chatRoomDtoList, pageable, chatRoomQueryResults.getTotal());
    }

    @GetMapping("/chatRoom/all")
    public Result<List<ChatRoomDto>> getAllChatRoom(@LoginAccountId Long loginAccountId) {
        List<ChatRoomDto> chatRoomDtoList = chatRoomService.findAllChatRoom(loginAccountId).stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
        return new Result<>(chatRoomDtoList);
    }

    @GetMapping("/chatRoom/{id}")
    public ChatRoomDto findById(@PathVariable("id") Long id) {
        return chatRoomService.findById(id);
    }
}
