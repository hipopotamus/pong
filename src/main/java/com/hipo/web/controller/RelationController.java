package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.domain.entity.Relation;
import com.hipo.service.RelationService;
import com.hipo.validator.RelationValidator;
import com.hipo.web.dto.AccountDto;
import com.hipo.web.dto.Result;
import com.hipo.web.dto.ResultMessage;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;
    private final RelationValidator relationValidator;

    @PostMapping("/relation/request/{accountId}")
    public ResultMessage requestFriend(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationValidator.validRequestFriend(loginAccountId, accountId);
        relationService.requestFriend(loginAccountId, accountId);
        return new ResultMessage("success friend request");
    }

    @PostMapping("/relation/request/accept/{accountId}")
    public ResultMessage acceptFriend(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationValidator.isBlockRelation(loginAccountId, accountId);
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
        relationValidator.isBlockRelation(loginAccountId, accountId);
        relationService.block(loginAccountId, accountId);
        return new ResultMessage("success block Account");
    }

    @PostMapping("/relation/unBlock/{accountId}")
    public ResultMessage unBlock(@LoginAccountId Long loginAccountId, @PathVariable Long accountId) {
        relationService.unBlock(loginAccountId, accountId);
        return new ResultMessage("success unBlock Account");
    }

    @GetMapping("relation/request/all")
    public Result<List<AccountDto>> findAllRequest(@LoginAccountId Long loginAccountId) {
        List<AccountDto> accountDtoList = relationService.findAllRequest(loginAccountId).stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new Result<>(accountDtoList);
    }

    @GetMapping("/relation/requests")
    public Page<AccountDto> findRequestsByPage(@LoginAccountId Long loginAccountId, Pageable pageable) {
        QueryResults<Relation> queryResults = relationService.findRequestsByPage(loginAccountId, pageable);
        List<AccountDto> accountDtoList = queryResults.getResults().stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new PageImpl<>(accountDtoList, pageable, queryResults.getTotal());
    }

    @GetMapping("/relation/requestFromOther/all")
    public Result<List<AccountDto>> findAllRequestFromOther(@LoginAccountId Long loginAccountId) {
        List<AccountDto> accountDtoList = relationService.findAllRequestFromOther(loginAccountId).stream()
                .map(relation -> new AccountDto(relation.getFromAccount()))
                .collect(Collectors.toList());
        return new Result<>(accountDtoList);
    }


    @GetMapping("/relation/requestsFromOther")
    public Page<AccountDto> findRequestsFromOtherByPage(@LoginAccountId Long loginAccountId, Pageable pageable) {
        QueryResults<Relation> queryResults = relationService.findRequestsFromOtherByPage(loginAccountId, pageable);
        List<AccountDto> accountDtoList = queryResults.getResults().stream()
                .map(relation -> new AccountDto(relation.getFromAccount()))
                .collect(Collectors.toList());
        return new PageImpl<>(accountDtoList, pageable, queryResults.getTotal());
    }

    @GetMapping("/relation/friend/all")
    public Result<List<AccountDto>> findAllFriend(@LoginAccountId Long loginAccountId, Pageable pageable) {
        List<AccountDto> accountDtoList = relationService.findAllFriend(loginAccountId).stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new Result<>(accountDtoList);
    }

    @GetMapping("/relation/friends")
    public Page<AccountDto> findFriends(@LoginAccountId Long loginAccountId, Pageable pageable) {
        QueryResults<Relation> queryResults = relationService.findFriendsByPage(loginAccountId, pageable);
        List<AccountDto> accountDtoList = queryResults.getResults().stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new PageImpl<>(accountDtoList, pageable, queryResults.getTotal());
    }

    @GetMapping("/relation/block/all")
    public Result<List<AccountDto>> findAllBlock(@LoginAccountId Long loginAccountId) {
        List<AccountDto> accountDtoList = relationService.findAllBlock(loginAccountId).stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new Result<>(accountDtoList);
    }

    @GetMapping("/relation/blocks")
    public Page<AccountDto> findBlockAccounts(@LoginAccountId Long loginAccountId, Pageable pageable) {
        QueryResults<Relation> queryResults = relationService.findBlocksByPage(loginAccountId, pageable);
        List<AccountDto> accountDtoList = queryResults.getResults().stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
        return new PageImpl<>(accountDtoList, pageable, queryResults.getTotal());
    }
}
