package com.hipo.test;

import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import com.hipo.service.ChatRoomService;
import com.hipo.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chatting")
@Log4j2
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageService messageService;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public String rooms(@RequestParam Long loginAccountId, Pageable pageable,
                        @RequestParam(value = "all", required = false) boolean all, Model model){
        model.addAttribute("list", chatRoomService.findAllChatRoom(loginAccountId));
        model.addAttribute("account", accountRepository.findById(loginAccountId).orElse(null));

        return "/chatting/rooms";
    }

    //채팅방 조회
    @GetMapping("/room/{roomId}")
    public String getRoom(@RequestParam Long loginAccountId, @PathVariable("roomId") Long roomId, Model model){

        model.addAttribute("messages", messageService.findChatRoomMessageBySlice(loginAccountId, roomId,
                PageRequest.of(0, 10)));
        model.addAttribute("room", chatRoomRepository.findById(roomId).orElse(null));
        model.addAttribute("account", accountRepository.findById(loginAccountId).orElse(null));
        return "/chatting/room";
    }
}
