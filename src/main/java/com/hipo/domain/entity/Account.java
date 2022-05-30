package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.exception.IllegalRequestException;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Where(clause = "deleted = false")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private int point = 0;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGenerateAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    //수정 관련//
    public void updateInfo(Account updatedAccount) {
        if (updatedAccount.nickname != null) {
            this.nickname = updatedAccount.getNickname();
        }
        if (updatedAccount.gender != null) {
            this.gender = updatedAccount.getGender();
        }
        if (updatedAccount.birthDate != null) {
            this.birthDate = updatedAccount.getBirthDate();
        }
    }

    public void updateProfileImg(String updatedProfileImgName) {
        this.profileImgName = updatedProfileImgName;
    }

    public void updatePassword(String updatedPassword) {
        this.password = updatedPassword;
    }
    //수정 관련//

    //게임 관련//
    public void win() {
        this.win += 1;
        point();
    }

    public void lose() {
        this.lose += 1;
        point();
    }

    public void point() {
        if (this.win == 0) {
            this.point = 0;
        } else {
            this.point = (win * 100) * (win / (win + lose));
        }
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

