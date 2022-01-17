package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.service.RelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    @PostMapping("/relation/request/accept/{accountId}")
    public ResultMessage acceptFriend(@ApiIgnore @LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.acceptFriend(loginAccountId, accountId);
        return new ResultMessage("success accept friend request");
    }

    @ApiOperation(value = "친구 요청 거절", notes = "회원 정보를 받아 해당 회원의 친구 요청을 거절 합니다.")
    @PostMapping("/relation/request/reject/{accountId}")
    public ResultMessage rejectFriend(@ApiIgnore @LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.rejectRequest(accountId, loginAccountId);
        return new ResultMessage("success reject friend request");
    }

    @ApiOperation(value = "친구 요청중 목록 조회", notes = "page와 size를 받으면 Page로 친구 요청중 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 친구 요청중 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/relation/requests")
    public Object findRequests(@ApiIgnore @LoginAccountId Long loginAccountId, @ApiIgnore Pageable pageable,
                                                     @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findRequests(loginAccountId, pageable, all));
        }
        return relationService.findRequests(loginAccountId, pageable, all);
    }

    @ApiOperation(value = "받은 친구 요청 목록 조회", notes = "page와 size를 받으면 Page로 받은 친구 요청 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 받은 친구 요청 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/relation/waitRequests")
    public Object findWaitingRequests(@ApiIgnore @LoginAccountId Long loginAccountId,  @ApiIgnore Pageable pageable,
                                                    @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findWaitingRequests(loginAccountId, pageable, all));
        }
        return relationService.findWaitingRequests(loginAccountId, pageable, all);
    }

    @ApiOperation(value = "친구 목록 조회", notes = "page와 size를 받으면 Page로 친구 목록을 조회합니다.\n" +
            "parameter를 입력하지 않으면 전체 친구 목록이 조회됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 사이즈", dataType = "int", paramType = "query")})
    @GetMapping("/relation/friends")
    public Object findFriend(@ApiIgnore @LoginAccountId Long loginAccountId, @ApiIgnore Pageable pageable,
                                       @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            return new Result<>(relationService.findFriends(loginAccountId, pageable, all));
        }
        return relationService.findFriends(loginAccountId, pageable, all);
    }
}
