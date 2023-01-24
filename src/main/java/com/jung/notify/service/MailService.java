package com.jung.notify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendResetPasswd(String email, String resetPasswd) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            simpleMailMessage.setTo(email);

            // 2. 메일 제목 설정
            simpleMailMessage.setSubject("Notify 임시 패스워드 안내입니다.");

            // 3. 메일 내용 설정
            simpleMailMessage.setText("안녕하세요. Notify 임시 패스워드 안내입니다. 임시 패스워드는 [ " + resetPasswd + " ] 입니다.");

            // 4. 메일 전송
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

    public void sendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            // 1. 메일 수신자 설정
            String[] receiveList = {"yongjung95@gmail.com"};

            // ArrayLis의 경우 배열로 변환이 필요함
//            ArrayList<String> receiveList = new ArrayList<>();
//            receiveList.add("test@naver.com");
//            receiveList.add("test@gmail.com");

//            String[] receiveList = (String[]) receiveList.toArray(new String[receiveList.size()]);

            simpleMailMessage.setTo("yongjung95@gmail.com");


            // 2. 메일 제목 설정
            simpleMailMessage.setSubject("test_title");

            // 3. 메일 내용 설정
            simpleMailMessage.setText("test_content");

            // 4. 메일 전송
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}

