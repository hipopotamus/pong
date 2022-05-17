package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.domain.entity.Account;
import com.hipo.utill.FileProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.service.AccountService;
import com.hipo.web.dto.AccountDto;
import com.hipo.web.dto.Result;
import com.hipo.web.dto.ResultMessage;
import com.hipo.web.form.*;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final FileProcessor fileProcessor;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.profile}")
    private String profileImgPath;

    @PostMapping("/account")
    public ResultMessage createAccount(@Valid @ModelAttribute AccountForm accountForm) throws IOException {
        String profileImgName = "default.jpeg";
        if (accountForm.getProfileImgFile() != null) {
            profileImgName = fileProcessor.storeFile(accountForm.getProfileImgFile(), profileImgPath);
        }

        String encodePassword = bCryptPasswordEncoder.encode(accountForm.getPassword());

        Account account = accountForm.toAccount(profileImgName, encodePassword);

        accountService.saveAccount(account);

        return new ResultMessage("success create Account");
    }

    @GetMapping("/account")
    public AccountDto getMyAccount(@LoginAccountId Long loginAccountId) {
        Account account = accountService.findById(loginAccountId);
        return new AccountDto(account);
    }

    @GetMapping("/account/{accountId}")
    public AccountDto getAccount(@PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        return new AccountDto(account);
    }

    @GetMapping("/account/all")
    public Result<List<AccountDto>> getAccountList() {
        List<AccountDto> accountDtoList = accountService.findAll().stream()
                .map(AccountDto::new)
                .collect(Collectors.toList());
        return new Result<>(accountDtoList);
    }

    @GetMapping("/accounts")
    public Page<AccountDto> getAccountByPage(AccountSearchCond accountSearchCond, Pageable pageable) {
        QueryResults<Account> accountQueryResults = accountService.findAccountByPage(accountSearchCond, pageable);
        List<AccountDto> accountDtoList = accountQueryResults.getResults().stream()
                .map(AccountDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(accountDtoList, pageable, accountQueryResults.getTotal());
    }

    @GetMapping("/account/profileImg/{accountId}")
    public ResponseEntity<Resource> getProfileImg(@PathVariable Long accountId) throws MalformedURLException {
        Account account = accountService.findById(accountId);
        String profileImgFullPath = fileProcessor.getFullPath(profileImgPath, account.getProfileImgName());

        UrlResource urlResource = new UrlResource("file:" + profileImgFullPath);
        String mediaType = fileProcessor.getMediaType(account.getProfileImgName());
        if (urlResource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType)
                    .body(urlResource);
        }
        throw new NonExistResourceException("profileImgFile이 존재하지 않습니다.");
    }

    @PutMapping("/account/info")
    public ResultMessage updateInfo(@LoginAccountId Long loginAccountId,
                                    @Valid @RequestBody AccountUpdateInfoForm accountUpdateInfoForm) {
        accountService.updateInfo(loginAccountId, accountUpdateInfoForm.toAccount());
        return new ResultMessage("success update Account Info");
    }

    @PostMapping("/account/profileImg")
    public ResultMessage updateProfileImg(@LoginAccountId Long loginAccountId,
                                          @Valid @ModelAttribute AccountProfileFileForm accountProfileFileForm)
            throws IOException {
        accountService.updateProfileImg(loginAccountId, accountProfileFileForm.getProfileImgFile());
        return new ResultMessage("success update Account profileImg");
    }

    @PutMapping("account/password")
    public ResultMessage updatePassword(@LoginAccountId Long loginAccountId,
                                        @Valid @RequestBody AccountUpdatePasswordForm accountUpdatePasswordForm) {
        accountService.updatePassword(loginAccountId, accountUpdatePasswordForm.getNewPassword());
        return new ResultMessage("success update password");
    }
}
