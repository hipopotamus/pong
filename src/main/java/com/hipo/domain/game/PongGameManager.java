package com.hipo.domain.game;

import com.hipo.dataobjcet.dto.PongGameFrameDto;
import com.hipo.domain.PongGameSet;
import com.hipo.domain.entity.Account;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PongGameManager {

    Map<Long, PongGameSet> pongGameFrameMap = new HashMap<>();
    private final SimpMessagingTemplate template;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public void createPongGameFrame(Long gameRoomId, Account master) {
        Ball ball = new Ball(400 - 10, 200 - 10, 20, 20, 2, 0, 800, 400);
        Bar leftBar = new Bar(50, 200 + 40, 20, 80, 2);
        Bar rightBar = new Bar(800 - 50 - 20, 200 + 40 +6, 20, 80, 2);
        PongGameFrame pongGameFrame = new PongGameFrame(rightBar, leftBar, ball, 800, 400, 8);
        pongGameFrame.setMaster(master);

        Timer timer = sendPongGameFrame(gameRoomId, pongGameFrame);
        pongGameFrameMap.put(gameRoomId, new PongGameSet(pongGameFrame, timer));
        timer.start();
    }

    public Timer sendPongGameFrame(Long gameRoomId, PongGameFrame pongGameFrame) {

        PongGameFrameDto pongGameFrameDto = new PongGameFrameDto(pongGameFrame);

        return new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                template.convertAndSend("/sub/pongGame/" + gameRoomId, pongGameFrameDto);

                if (pongGameFrame.isEnd()) {
                    accountService.match(pongGameFrame);
                    pongGameFrame.setEnd(false);
                }
            }
        });
    }

    public void deleteRoom(Long gameRoomId) {
        PongGameFrame pongGameFrame = pongGameFrameMap.get(gameRoomId).getPongGameFrame();
        Timer timer = pongGameFrameMap.get(gameRoomId).getTimer();

        pongGameFrame.end();
        timer.stop();
        pongGameFrameMap.remove(gameRoomId);
    }

    public PongGameFrame findPongGameFrame(Long gameRoomId) {
        return pongGameFrameMap.get(gameRoomId).getPongGameFrame();
    }

    public void attendRoomByChallenger(Long gameRoomId, Long accountId) {
        Account challenger = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        PongGameFrame pongGameFrame = findPongGameFrame(gameRoomId);
        pongGameFrame.setChallenger(challenger);
    }

    public void attendRoomBySpectator(Long gameRoomId, Long accountId) {
        Account spectator = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        findPongGameFrame(gameRoomId).appendSpectator(spectator.getNickname());
    }
}
