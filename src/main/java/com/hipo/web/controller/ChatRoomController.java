package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.service.ChatRoomService;
import com.hipo.web.dto.ChatRoomDto;
import com.hipo.web.dto.Result;
import com.hipo.web.dto.ResultMessage;
import com.hipo.web.form.ChatRoomMasterForm;
import com.hipo.web.form.ChatRoomNameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Object findChatRoom(@LoginAccountId Long loginAccountId, Pageable pageable,
                                                      @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(chatRoomService.findChatRoom(loginAccountId, pageable, all));
        }
        return chatRoomService.findChatRoom(loginAccountId, pageable, all);
    }

    @GetMapping("/chatRoom/{id}")
    public ChatRoomDto findById(@PathVariable("id") Long id) {
        return chatRoomService.findById(id);
    }
}