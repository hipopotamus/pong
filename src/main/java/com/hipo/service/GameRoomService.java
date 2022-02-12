package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.GameRoom;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.GameRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final AccountRepository accountRepository;
    private final PongGameService pongGameManager;

    @Transactional
    public void createGameRoom(Long accountId, String name) {
        Account master = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        GameRoom gameRoom = gameRoomRepository.save(new GameRoom(name));

        pongGameManager.createPongGameFrame(gameRoom.getId(), master);
    }

    @Transactional
    public void deleteGameRoom(Long accountId, Long gameRoomId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 GameRoom을 찾을 수 없습니다."));
        pongGameManager.deleteRoom(gameRoomId);
        gameRoom.softDelete();
    }
}
