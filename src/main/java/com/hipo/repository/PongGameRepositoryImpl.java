package com.hipo.repository;

import com.hipo.domain.PongGameSet;
import com.hipo.domain.game.PongGameFrame;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class PongGameRepositoryImpl implements PongGameRepository{

    Map<Long, PongGameSet> pongGameFrameMap = new HashMap<>();


    @Override
    public void save(Long gameRoomId, PongGameSet pongGameSet) {
        pongGameFrameMap.put(gameRoomId, pongGameSet);
    }

    @Override
    public PongGameFrame findPongGameFrame(Long gameRoomId) {
        return pongGameFrameMap.get(gameRoomId).getPongGameFrame();
    }

    @Override
    public Timer findTimer(Long gameRoomId) {
        return pongGameFrameMap.get(gameRoomId).getTimer();
    }

    @Override
    public void remove(Long gameRoomId) {
        pongGameFrameMap.remove(gameRoomId);
    }


}
