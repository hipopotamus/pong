package com.hipo.service;

import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.domain.game.PongGameFrame;
import com.hipo.domain.processor.FileProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final FileProcessor fileProcessor;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.profile}")
    private String profileImgPath;

    @SneakyThrows
    @Transactional
    public Account createAccount(String username, String password, String nickname, MultipartFile multipartFile,
                                 Gender gender, LocalDate birthDate) throws IOException {
        String storeFilePath = fileProcessor.storeFile(multipartFile, profileImgPath);
        String encodePassword = bCryptPasswordEncoder.encode(password);

        Account account = Account.builder()
                .username(username)
                .password(encodePassword)
                .nickname(nickname)
                .profileImgPath(storeFilePath)
                .role(Role.User)
                .gender(gender)
                .birthDate(birthDate)
                .build();

        return accountRepository.save(account);
    }

    @Transactional
    public void updateNickname(Long loginAccountId, String nickname) {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        account.updateNickname(nickname);
    }

    @Transactional
    public void updateProfileImg(Long loginAccountId, MultipartFile multipartFile) throws IOException {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        String storeFilePath = fileProcessor.storeFile(multipartFile, profileImgPath);

        account.updateProfileImg(storeFilePath);
    }

    @Transactional
    public void updateGender(Long loginAccountId, Gender gender) {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        account.updateGender(gender);
    }

    @Transactional
    public void updateBirthDate(Long loginAccountId, LocalDate birthDate) {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        account.updateBirthDate(birthDate);
    }

    public AccountDto findByNickname(String nickname) {
        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new NonExistResourceException("해당 nickname을 갖는 Account를 찾을 수 없습니다."));

        return new AccountDto(account);
    }

    @Transactional
    public void match(PongGameFrame pongGameFrame) {
        Account master = accountRepository.findById(pongGameFrame.getMaster().getId())
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account challenger = accountRepository.findById(pongGameFrame.getChallenger().getId())
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        if (pongGameFrame.getMasterWinNumber() == 3) {
            master.win();
            challenger.lose();
        }
        if (pongGameFrame.getChallengerWinNumber() == 3) {
            challenger.win();
            master.lose();
        }

        pongGameFrame.setMaster(master);
        pongGameFrame.setChallenger(challenger);
    }

}
