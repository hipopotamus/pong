package com.hipo.service;

import com.hipo.web.dto.EmailMessage;
import com.hipo.domain.entity.Account;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final AccountRepository accountRepository;

    @Transactional
    public void sendSignUpConfirmEmail(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        if (account.getEmailCheckTokenGenerateAt() != null && !account.canSendEmailToken()) {
            throw new IllegalRequestException("이메일 확인 메일은 5분 간격으로 발송 가능합니다.");
        }

        account.generateEmailToken();
        Context context = new Context();
        context.setVariable("username", account.getUsername());
        context.setVariable("message", "이메일 인증 번호");
        context.setVariable("token", account.getEmailCheckToken());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getUsername())
                .subject("회원가입 이메일 인증")
                .message(message)
                .build();

        sendEmail(emailMessage);
    }

    private void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void confirmEmail(Long accountId, String emailToken) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        account.verifyEmail(emailToken);
    }
}
