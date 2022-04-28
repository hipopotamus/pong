package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.*;
import com.hipo.domain.processor.FileProcessor;
import com.hipo.exception.IllegalFormException;
import com.hipo.service.AccountService;
import com.hipo.validator.AccountBirthDateFormValidator;
import com.hipo.validator.AccountFormValidator;
import com.hipo.validator.AccountNicknameFormValidator;
import com.hipo.validator.AccountProfileFileFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountFormValidator accountFormValidator;
    private final AccountNicknameFormValidator accountNicknameFormValidator;
    private final AccountProfileFileFormValidator accountProfileFileFormValidator;
    private final AccountBirthDateFormValidator accountBirthDateFormValidator;
    private final FileProcessor fileProcessor;


    @Value("${file.profile}")
    private String profileImgPath;

    @InitBinder("accountForm")
    public void accountFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountFormValidator);
    }

    @InitBinder("accountNicknameForm")
    public void accountNicknameFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountNicknameFormValidator);
    }

    @InitBinder("accountProfileFileForm")
    public void accountProfileFileFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountProfileFileFormValidator);
    }

    @InitBinder("accountBirthDateForm")
    public void accountBirthDateFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountBirthDateFormValidator);
    }

    @PostMapping("/account")
    public ResultMessage createAccount(@Valid @ModelAttribute AccountForm accountForm,
                                       Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.createAccount(accountForm.getUsername(), accountForm.getPassword(), accountForm.getNickname(),
                accountForm.getProfileFile(), accountForm.getGender(), accountForm.getBirthDate());

        return new ResultMessage("success create Account");
    }

    @PostMapping("/account/nickname")
    public ResultMessage updateNickname(@LoginAccountId Long loginAccountId,
                                        @Valid @RequestBody AccountNicknameForm accountNicknameForm,
                                        Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateNickname(loginAccountId, accountNicknameForm.getNickname());

        return new ResultMessage("success update Account nickname");
    }

    @PostMapping("/account/profileImg")
    public ResultMessage updateProfileImg(@LoginAccountId Long loginAccountId,
                                          @Valid @ModelAttribute AccountProfileFileForm accountProfileFileForm,
                                          Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateProfileImg(loginAccountId, accountProfileFileForm.getProfileFile());

        return new ResultMessage("success update Account profileImg");
    }

    @PostMapping("/account/gender")
    public ResultMessage updateGender(@LoginAccountId Long loginAccountId,
                                      @Valid @RequestBody AccountGenderForm accountGenderForm,
                                      Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateGender(loginAccountId, accountGenderForm.getGender());

        return new ResultMessage("success update Account gender");
    }

    @PostMapping("/account/birthDate")
    public ResultMessage updateBirthDate(@LoginAccountId Long loginAccountId,
                                          @Valid @RequestBody AccountBirthDateForm accountBirthDateForm,
                                          Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateBirthDate(loginAccountId, accountBirthDateForm.getBirthDate());

        return new ResultMessage("success update Account birthDate");
    }

    @GetMapping("/account/{accountId}")
    public AccountDto findAccount(@PathVariable Long accountId) {
        return accountService.findById(accountId);
    }

    @GetMapping("/account/profileImg/{accountId}")
    public ResponseEntity<Resource> getProfileImg(@PathVariable Long accountId) throws MalformedURLException {
        AccountDto accountDto = accountService.findById(accountId);
        String profileImgFullPath = fileProcessor.getFullPath(profileImgPath, accountDto.getProfileImgName());

        UrlResource urlResource = new UrlResource("file:" + profileImgFullPath);
        String mediaType = fileProcessor.getMediaType(accountDto.getProfileImgName());
        if (urlResource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType)
                    .body(urlResource);
        }
        return ResponseEntity.notFound().build();
    }

}
