package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.MessageResult;
import com.hipo.dataobjcet.form.*;
import com.hipo.exception.IllegalFormException;
import com.hipo.service.AccountService;
import com.hipo.validator.AccountBirthDateFormValidator;
import com.hipo.validator.AccountFormValidator;
import com.hipo.validator.AccountNicknameFormValidator;
import com.hipo.validator.AccountProfileFileFormValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = {"2. Account"})
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountFormValidator accountFormValidator;
    private final AccountNicknameFormValidator accountNicknameFormValidator;
    private final AccountProfileFileFormValidator accountProfileFileFormValidator;
    private final AccountBirthDateFormValidator accountBirthDateFormValidator;

    @InitBinder("accountForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountFormValidator);
        webDataBinder.addValidators(accountNicknameFormValidator);
        webDataBinder.addValidators(accountProfileFileFormValidator);
        webDataBinder.addValidators(accountBirthDateFormValidator);
    }

    @ApiOperation(value = "회원 가입", notes = "회원 정보를 받아 새로운 회원을 생성합니다.")
    @PostMapping("/account")
    public MessageResult createAccount(@Valid @ModelAttribute AccountForm accountForm,
                                       @ApiIgnore Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.createAccount(accountForm.getUsername(), accountForm.getPassword(), accountForm.getNickname(),
                accountForm.getProfileFile(), accountForm.getGender(), accountForm.getBirtDate());

        return new MessageResult("success create Account");
    }

    @ApiOperation(value = "내 닉네임 수정", notes = "변경할 닉네임을 받아 회원의 닉네임을 수정합니다.")
    @PostMapping("/account/nickname")
    public MessageResult updateNickname(@LoginAccountId Long loginAccountId,
                                               @Valid @RequestBody AccountNicknameForm accountNicknameForm,
                                               @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateNickname(loginAccountId, accountNicknameForm.getNickname());

        return new MessageResult("success update Account nickname");
    }

    @ApiOperation(value = "내 프로필 이미지 수정", notes = "변경할 프로필 이미지를 받아 회원의 프로필 이미지를 수정합니다.")
    @PostMapping("/account/profileImg")
    public MessageResult updateProfileImg(@ApiIgnore @LoginAccountId Long loginAccountId,
                                          @Valid @RequestBody AccountProfileFileForm accountProfileFileForm,
                                          @ApiIgnore Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateProfileImg(loginAccountId, accountProfileFileForm.getProfileFile());

        return new MessageResult("success update Account profileImg");
    }

    @ApiOperation(value = "내 성별 수정", notes = "변경할 성별을 받아 회원의 성별을 수정합니다.")
    @PostMapping("/account/gender")
    public MessageResult updateGender(@ApiIgnore @LoginAccountId Long loginAccountId,
                                      @Valid @RequestBody AccountGenderForm accountGenderForm,
                                      @ApiIgnore Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateGender(loginAccountId, accountGenderForm.getGender());

        return new MessageResult("success update Account gender");
    }

    @ApiOperation(value = "내 생년월일 수정", notes = "변경할 생년월일을 받아 회원의 생년원일을 수정합니다.")
    @PostMapping("/account/birthDate")
    public MessageResult updateProfileImg(@ApiIgnore @LoginAccountId Long loginAccountId,
                                          @Valid @RequestBody AccountBirthDateForm accountBirthDateForm,
                                          @ApiIgnore Errors errors) throws IOException {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountService.updateBirthDate(loginAccountId, accountBirthDateForm.getBirtDate());

        return new MessageResult("success update Account birthDate");
    }

}
