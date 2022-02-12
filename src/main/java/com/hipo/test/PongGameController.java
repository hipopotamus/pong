package com.hipo.test;

import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.domain.entity.Account;
import com.hipo.domain.game.Ball;
import com.hipo.domain.game.Bar;
import com.hipo.domain.game.PongGameFrame;
import com.hipo.service.PongGameService;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PongGameController {

    private final PongGameService pongGameManager;
    private final AccountRepository accountRepository;

    @GetMapping("/pongGame/test")
    public ResultMessage test() {
        Ball ball = new Ball(400, 200, 20, 20, 2, 0, 800, 400);
        Bar leftBar = new Bar(10, 390, 20, 350, 2, 800, 400);
        Bar rightBar = new Bar(790, 200, 20, 80, 2, 800, 400);

        Account master = accountRepository.findByUsername("test1@test.com").orElse(null);
        Account challenger = accountRepository.findByUsername("test2@test.com").orElse(null);

        PongGameFrame pongGameFrame = new PongGameFrame(rightBar, leftBar, ball, 800, 400 , 8);
        pongGameFrame.setMaster(master);
        pongGameFrame.setChallenger(challenger);
        pongGameFrame.start();


        return new ResultMessage("success start");
    }

    @GetMapping("/pongGame/gameRoom/{roomId}")
    public String gameRoom(@RequestParam Long loginAccountId, @PathVariable("roomId") Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "/pongGame/gameRoom";
    }
}
