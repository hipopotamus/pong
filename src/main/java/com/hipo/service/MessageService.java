package com.hipo.service;

import com.hipo.web.dto.MessageDto;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.entity.Message;
import com.hipo.domain.entity.enums.MessageType;
import com.hipo.utill.JudgeProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import com.hipo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JudgeProcessor judgeProcessor;

    @Transactional
    public Message createMessage(String message, MessageType messageType, Long accountId, Long chatRoomId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));
        judgeProcessor.isChatRoomMember(account, chatRoom);

        return messageRepository.save(new Message(message, messageType, account, chatRoom));
    }

    public Iterable<MessageDto> findChatRoomMessage(Long loginAccountId, Long chatRoomId, Pageable pageable, boolean all) {

        if (all) {
            List<MessageDto> messageList = messageRepository.findAllChatRoomMessage(loginAccountId, chatRoomId).stream()
                    .map(MessageDto::new)
                    .collect(Collectors.toList());
            Collections.reverse(messageList);
            return messageList;
        }

        Slice<Message> chatRoomMessage = messageRepository.findChatRoomMessage(loginAccountId, chatRoomId, pageable);
        List<MessageDto> messageDtoList = chatRoomMessage.stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());
        Collections.reverse(messageDtoList);

        return new SliceImpl<>(messageDtoList, pageable, chatRoomMessage.hasNext());
    }
}
