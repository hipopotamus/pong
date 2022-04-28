package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.exception.IllegalRequestException;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted = false")
public class Account extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String profileImgName;

    private int win = 0;

    private int lose = 0;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGenerateAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    protected Account() {
    }

    //회원가입 생성자
    @Builder
    public Account(String username, String password, String nickname, String profileImgPath, Role role, Gender gender,
                   LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImgName = profileImgPath;
        this.role = role;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    //수정 관련
    public void updateNickname(String nickname) {
       this.nickname = nickname;
    }

    public void updateProfileImg(String profileImgName) {
        this.profileImgName = profileImgName;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    //수정 관련//

    //게임 관련//
    public void win() {
        win += 1;
    }

    public void lose() {
        lose += 1;
    }
    //게임 관련//

    //email 인증 관련//
    public void generateEmailToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGenerateAt = LocalDateTime.now();
    }

    public void verifyEmail(String emailCheckToken) {
        if (this.emailCheckToken.equals(emailCheckToken)) {
            emailVerified = true;
            this.role = Role.User;
            return;
        }
        throw new IllegalRequestException("emailToken이 맞지 않습니다.");
    }
    public boolean canSendEmailToken() {
        return this.emailCheckTokenGenerateAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }
    //email 인증 관련//

    public void settingRole(Role role) {
        this.role = role;
    }
}

