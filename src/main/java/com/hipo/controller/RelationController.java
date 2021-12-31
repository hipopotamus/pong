package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.service.RelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = {"3. Relation"})
@RestController
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @ApiOperation(value = "친구 요청", notes = "회원 정보를 받아 해당 회원에게 친구 요청을 합니다.")
    @PostMapping("/relation/request/{accountId}")
    public ResultMessage requestFriend(@ApiIgnore @LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.requestFriend(loginAccountId, accountId);
        return new ResultMessage("success friend request");
    }

    @ApiOperation(value = "친구 요청 승락", notes = "회원 정보를 받아 해당 회원의 친구 요청을 승락 합니다.")
    @PostMapping("/relation/friend/{accountId}")
    public ResultMessage acceptFriend(@ApiIgnore @LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.acceptFriend(loginAccountId, accountId);
        return new ResultMessage("success accept friend request");
    }

    @ApiOperation(value = "친구 목록 조회", notes = "회원 정보를 받아 해당 회원의 친구 목록을 조회 합니다.")
    @GetMapping("/relation/friends")
    public List<AccountDto> findFriend(@ApiIgnore @LoginAccountId Long loginAccountId) {
        return relationService.findFriends(loginAccountId);
    }

    @ApiOperation(value = "친구 요청 목록 조회", notes = "로그인한 회원의 친구 요청 목록을 조회 합니다.")
    @GetMapping("/relation/requests")
    public List<AccountDto> findRequests(@ApiIgnore @LoginAccountId Long loginAccountId) {
        return relationService.findRequests(loginAccountId);
    }

    @ApiOperation(value = "받은 친구 요청 목록 조회",  notes = "로그인한 회원이 받은 친구 요청 목록을 조회 합니다.")
    @GetMapping("/relation/waitRequests")
    public List<AccountDto> findWaitingRequests(@ApiIgnore @LoginAccountId Long loginAccountId) {
        return relationService.findWaitingRequests(loginAccountId);
    }
}
