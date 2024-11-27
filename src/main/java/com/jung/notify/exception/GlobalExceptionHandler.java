package com.jung.notify.exception;

import com.jung.notify.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Executor taskExecutor;
    private final MessageService messageService;

    @Value("${notify.authorization}")
    private String authorization;

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception ex) {
        // 예외 처리 로직

        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("❌ Notify 에러 발생 ❌\n \n");
        stringBuffer.append(ex.getMessage());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", stringBuffer.toString());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + authorization);
        header.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, header);
        String serverUrl = "https://notify-api.line.me/api/notify";

        RestTemplate restTemplate = new RestTemplate();

        taskExecutor.execute(() -> {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
        });
    }
}
