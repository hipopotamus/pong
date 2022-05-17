package com.hipo.service;

import com.hipo.web.dto.ChatRoomDto;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.AccountChatRoom;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final AccountChatRoomRepository accountChatRoomRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public ChatRoom createChatRoom(Long accountId, String chatRoomName) {
        Account activeAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(chatRoomName, activeAccount));
        accountChatRoomRepository.save(new AccountChatRoom(activeAccount, chatRoom));

        return chatRoom;
    }

    @Transactional
    public void updateChatRoomName(Long accountId, Long chatRoomId, String updateChatRoomName) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));

        if (chatRoom.getMasterAccount() != account) {
            throw new IllegalRequestException("해당 Account는 ChatRoom의 master가 아닙니다.");
        }

        chatRoom.updateChatRoomName(updateChatRoomName);
    }

    @Transactional
    public void updateChatRoomMasterAccount(Long accountId, Long chatRoomId, Long updateMasterAccountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account updateMasterAccount = accountRepository.findById(updateMasterAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));

        if (chatRoom.getMasterAccount() != account) {
            throw new IllegalRequestException("해당 Account는 ChatRoom의 master가 아닙니다.");
        }

        chatRoom.updateChatRoomMaster(updateMasterAccount);
    }

    public Iterable<ChatRoomDto> findChatRoom(Long accountId, Pageable pageable, boolean all) {
        if (all) {
            return chatRoomRepository.findAllChatRoom(accountId).stream()
                    .map(ChatRoomDto::new)
                    .collect(Collectors.toList());
        }
        Page<ChatRoom> chatRoom = chatRoomRepository.findChatRoom(accountId, pageable);
        List<ChatRoomDto> chatRoomDtoList = chatRoom.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(chatRoomDtoList, pageable, chatRoom.getTotalElements());
    }

    public ChatRoomDto findById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));
        return new ChatRoomDto(chatRoom);
    }
}
