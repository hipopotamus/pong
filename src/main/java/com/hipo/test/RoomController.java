package com.hipo.test;

import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import com.hipo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public String rooms(@RequestParam Long loginAccountId, @ApiIgnore Pageable pageable,
                        @RequestParam(value = "all", required = false) boolean all, Model model){
        model.addAttribute("list", chatRoomService.findChatRoom(loginAccountId, pageable, all));
        model.addAttribute("account", accountRepository.findById(loginAccountId).orElse(null));

        return "/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room/{roomId}")
    public String getRoom(@RequestParam Long loginAccountId, @PathVariable("roomId") Long roomId, Model model){

        model.addAttribute("room", chatRoomRepository.findById(roomId).orElse(null));
        model.addAttribute("account", accountRepository.findById(loginAccountId).orElse(null));
        return "/chat/room";
    }
}
