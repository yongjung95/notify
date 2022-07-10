package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.MessageLog;
import com.jung.notify.domain.MessageLogResultCode;
import com.jung.notify.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    @Transactional
    public void sendMessage(MultiValueMap<String, Object> body, Member member) {
        String authorization = member.getLineToken();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + authorization);
        header.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, header);
        String serverUrl = "https://notify-api.line.me/api/notify";

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

            if(response.getStatusCodeValue() == 200){
                MessageLog messageLog = MessageLog.builder()
                        .member(member)
                        .messageLogResultCode(MessageLogResultCode.SUCCESS)
                        .created(LocalDateTime.now())
                        .build();

                messageRepository.createMessageLog(messageLog);
            }else{
                MessageLog messageLog = MessageLog.builder()
                        .member(member)
                        .messageLogResultCode(MessageLogResultCode.FAILED)
                        .failedMessage(response.getBody())
                        .created(LocalDateTime.now())
                        .build();

                messageRepository.createMessageLog(messageLog);
            }
        }catch (Exception e){
            MessageLog messageLog = MessageLog.builder()
                    .member(member)
                    .messageLogResultCode(MessageLogResultCode.FAILED)
                    .failedMessage(e.getMessage())
                    .created(LocalDateTime.now())
                    .build();

            messageRepository.createMessageLog(messageLog);
        }
    }
}
