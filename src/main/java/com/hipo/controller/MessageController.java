package com.hipo.controller;

import com.hipo.dataobjcet.dto.MessageDto;
import com.hipo.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"6. Message"})
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "채팅방 메시지 목록 조회", notes = "page와 size를 받으면 Slice로 채팅방 메시지 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 채팅방 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/message/{chatRoomId}")
    public Iterable<MessageDto> findChatRoomMessage(@PathVariable("chatRoomId") Long chatRoomId, Pageable pageable,
                                                    @RequestParam(value = "paged", required = false) boolean paged) {
        return messageService.findChatRoomMessage(chatRoomId, pageable, paged);
    }

}
