package com.hipo.web.dto;

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

    private int point;

    public AccountDto(Account account) {
       this.id = account.getId();
       this.username = account.getUsername();
       this.nickname = account.getNickname();
       this.profileImgName = account.getProfileImgName();
       this.gender = account.getGender();
       this.birthDate = account.getBirthDate();
       this.win = account.getWin();
       this.lose = account.getLose();
       this.point = account.getPoint();
    }
}
