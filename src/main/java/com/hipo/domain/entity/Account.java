package com.hipo.domain.entity;

import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "delete = false")
public class Account {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String profileImgPath;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    protected Account() {
    }

    //회원가입 생성자
    public Account(String username, String password, String nickname, String profileImgPath, Role role, Gender gender, LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImgPath = profileImgPath;
        this.role = role;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    //수정 관련
    public void updateNickname(String nickname) {
       this.nickname = nickname;
    }

    public void updateProfileImg(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    //수정 관련//

    public void settingRole(Role role) {
        this.role = role;
    }
}
