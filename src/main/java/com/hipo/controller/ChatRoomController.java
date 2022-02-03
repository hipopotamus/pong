package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ChatRoomDto;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.ChatRoomMasterForm;
import com.hipo.dataobjcet.form.ChatRoomNameForm;
import com.hipo.exception.IllegalFormException;
import com.hipo.service.ChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"4. ChatRoom"})
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @ApiOperation(value = "채팅방 생성", notes = "채팅방 이름을 받고 채팅방을 생성합니다.")
    @PostMapping("/chatRoom")
    public ResultMessage createChatRoom(@ApiIgnore @LoginAccountId Long loginAccountId,
                                        @Valid @RequestBody ChatRoomNameForm chatRoomNameForm,
                                        @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        chatRoomService.createChatRoom(loginAccountId, chatRoomNameForm.getName());

        return new ResultMessage("success create ChatRoom");
    }

    @ApiOperation(value = "채팅방 이름 수정", notes = "채팅방 id와 이름을 받아 채팅방의 이름을 수정합니다.")
    @PostMapping("/chatRoom/name")
    public ResultMessage updateChatRoomName(@ApiIgnore @LoginAccountId Long loginAccountId,
                                            @Valid @RequestBody ChatRoomNameForm chatRoomNameForm,
                                            @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        chatRoomService.updateChatRoomName(loginAccountId, chatRoomNameForm.getChatRoomId(), chatRoomNameForm.getName());

        return new ResultMessage("success update ChatRoomName");
    }

    @ApiOperation(value = "채팅방 주인 수정", notes = "채팅방 id와 accountId를 받아 채팅방의 주인을 수정합니다.")
    @PostMapping("/chatRoom/master")
    public ResultMessage updateChatRoomMaster(@ApiIgnore @LoginAccountId Long loginAccountId,
                                              @Valid @RequestBody ChatRoomMasterForm chatRoomMasterForm,
                                              @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        chatRoomService.updateChatRoomMasterAccount(loginAccountId, chatRoomMasterForm.getChatRoomId(), chatRoomMasterForm.getAccountId());

        return new ResultMessage("success update ChatRoomName");
    }

    @ApiOperation(value = "채팅방 목록 조회", notes = "page와 size를 받으면 Page로 채팅방 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 채팅방 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/chatRooms")
    public Object findChatRoom(@ApiIgnore @LoginAccountId Long loginAccountId, @ApiIgnore Pageable pageable,
                                                      @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(chatRoomService.findChatRoom(loginAccountId, pageable, all));
        }
        return chatRoomService.findChatRoom(loginAccountId, pageable, all);
    }

    @ApiOperation(value = "채팅방 조회", notes = "채팅방 아이디를 받고 해당 채팅방을 조회합니다.")
    @GetMapping("/chatRoom/{id}")
    public ChatRoomDto findById(@PathVariable("id") Long id) {
        return chatRoomService.findById(id);
    }
}
