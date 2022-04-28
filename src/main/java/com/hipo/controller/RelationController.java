package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @PostMapping("/relation/request/{accountId}")
    public ResultMessage requestFriend(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.requestFriend(loginAccountId, accountId);
        return new ResultMessage("success friend request");
    }

    @PostMapping("/relation/request/accept/{accountId}")
    public ResultMessage acceptFriend(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.acceptFriend(loginAccountId, accountId);
        return new ResultMessage("success accept friend request");
    }

    @PostMapping("/relation/request/reject/{accountId}")
    public ResultMessage rejectFriend(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.rejectRequest(accountId, loginAccountId);
        return new ResultMessage("success reject friend request");
    }

    @PostMapping("/relation/block/{accountId}")
    public ResultMessage block(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.block(loginAccountId, accountId);
        return new ResultMessage("success block Account");
    }

    @GetMapping("/relation/requests")
    public Object findRequests(@LoginAccountId Long loginAccountId, Pageable pageable,
                               @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findRequests(loginAccountId, pageable, all));
        }
        return relationService.findRequests(loginAccountId, pageable, all);
    }

    @GetMapping("/relation/waitRequests")
    public Object findWaitingRequests(@LoginAccountId Long loginAccountId, Pageable pageable,
                                      @RequestParam(required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findWaitingRequests(loginAccountId, pageable, all));
        }
        return relationService.findWaitingRequests(loginAccountId, pageable, all);
    }

    @GetMapping("/relation/friends")
    public Object findFriends(@LoginAccountId Long loginAccountId, Pageable pageable,
                                       @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findFriends(loginAccountId, pageable, all));
        }
        return relationService.findFriends(loginAccountId, pageable, all);
    }

    @GetMapping("/relation/blocks")
    public Object findBlockAccounts(@LoginAccountId Long loginAccountId, Pageable pageable,
                             @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findBlockAccounts(loginAccountId, pageable, all));
        }
        return relationService.findBlockAccounts(loginAccountId, pageable, all);
    }
}
