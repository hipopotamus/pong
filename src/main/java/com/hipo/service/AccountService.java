package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.game.PongGameFrame;
import com.hipo.utill.FileProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.web.form.AccountSearchCond;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final FileProcessor fileProcessor;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.profile}")
    private String profileImgPath;

    @Transactional
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    public Account findByNickname(String nickname) {
        return accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new NonExistResourceException("해당 nickname을 갖는 Account를 찾을 수 없습니다."));
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 accountId를 갖는 Account를 찾을 수 없습니다."));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public QueryResults<Account> findAccountByPage(AccountSearchCond accountSearchCond, Pageable pageable) {
        return accountRepository.findAccountsByPage(accountSearchCond, pageable);
    }

    @Transactional
    public void updateInfo(Long loginAccountId, Account toAccount) {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        account.updateInfo(toAccount);
    }

    @Transactional
    public void updateProfileImg(Long loginAccountId, MultipartFile multipartFile) throws IOException {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        String storeFilePath = fileProcessor.storeFile(multipartFile, profileImgPath);

        account.updateProfileImg(storeFilePath);

        File profileImgFile = new File(profileImgPath + account.getProfileImgName());
        if (profileImgFile.exists()) {
            profileImgFile.delete();
        }
    }

    @Transactional
    public void updatePassword(Long loginAccountId, String updatedPassword) {
        Account account = accountRepository.findById(loginAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        String password = bCryptPasswordEncoder.encode(updatedPassword);
        account.updatePassword(password);
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
