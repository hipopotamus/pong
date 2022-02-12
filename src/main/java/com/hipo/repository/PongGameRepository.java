package com.hipo.repository;

import com.hipo.domain.PongGameSet;
import com.hipo.domain.game.PongGameFrame;

import javax.swing.*;

public interface PongGameRepository {

    void save(Long gameRoomId, PongGameSet pongGameSet);

    PongGameFrame findPongGameFrame(Long gameRoomId);

    Timer findTimer(Long gameRoomId);

    void remove(Long gameRoomId);
}
