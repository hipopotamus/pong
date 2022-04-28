package com.hipo.dataobjcet.dto;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;

    private String username;

    private String nickname;

    private String profileImgName;

    private Gender gender;

    private LocalDate birthDate;

    private int win;

    private int lose;

    public AccountDto(Account account) {
       id = account.getId();
       username = account.getUsername();
       nickname = account.getNickname();
       profileImgName = account.getProfileImgName();
       gender = account.getGender();
       birthDate = account.getBirthDate();
       win = account.getWin();
       lose = account.getLose();
    }
}
