package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ChatRoomDto;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.ChatRoomMasterForm;
import com.hipo.dataobjcet.form.ChatRoomNameForm;
import com.hipo.exception.IllegalFormException;
import com.hipo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chatRoom")
    public ResultMessage createChatRoom(@LoginAccountId Long loginAccountId,
                                        @Valid @RequestBody ChatRoomNameForm chatRoomNameForm,
                                        Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        chatRoomService.createChatRoom(loginAccountId, chatRoomNameForm.getName());

        return new ResultMessage("success create ChatRoom");
    }

    @PostMapping("/chatRoom/name")
    public ResultMessage updateChatRoomName(@LoginAccountId Long loginAccountId,
                                            @Valid @RequestBody ChatRoomNameForm chatRoomNameForm,
                                            Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        chatRoomService.updateChatRoomName(loginAccountId, chatRoomNameForm.getChatRoomId(), chatRoomNameForm.getName());

        return new ResultMessage("success update ChatRoomName");
    }

    @PostMapping("/chatRoom/master")
    public ResultMessage updateChatRoomMaster(@LoginAccountId Long loginAccountId,
                                              @Valid @RequestBody ChatRoomMasterForm chatRoomMasterForm,
                                              Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

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
