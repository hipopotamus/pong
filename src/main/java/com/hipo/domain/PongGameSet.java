package com.hipo.domain;

import com.hipo.domain.game.PongGameFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PongGameSet {

    private PongGameFrame pongGameFrame;

    private Timer timer;
}
