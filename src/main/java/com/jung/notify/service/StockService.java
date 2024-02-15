package com.jung.notify.service;

import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockApiInfo;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.dto.StockDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.StockApiInfoRepository;
import com.jung.notify.repository.StockManageRepository;
import com.jung.notify.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    private final MemberService memberService;

    private final MessageService messageService;

    private final StockManageRepository stockManageRepository;

    private final StockApiInfoRepository stockApiInfoRepository;

    @Value("${korea.stock.appkey}")
    private String appKey;

    @Value("${korea.stock.appsecret}")
    private String appSecret;

    @Value("${korea.stock.tr_id}")
    private String trId;



    public Page<StockDto.SelectStock> selectStockList(String corpName, Pageable pageable, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        return stockRepository.selectStockList(corpName, pageable, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
    }

    public boolean saveStockManage(Long stockId, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        Stock stock = stockRepository.findById(stockId).orElseGet(null);

        if (stock == null) {
            return false;
        }

        StockManage stockManage = StockManage.builder()
                .stock(stock)
                .member(MemberMapper.INSTANCE.selectMemberToMember(selectMember))
                .build();

        stockManageRepository.save(stockManage);

        return true;
    }

    public Page<StockDto.SelectStock> selectStockManageList(Pageable pageable, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        return stockManageRepository.selectStockManageList(pageable, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
    }

    public void sendMorningStockPriceList(boolean isMorning) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        List<MemberDto.SelectMember> selectMemberList = memberService.findAllMember();

        for (MemberDto.SelectMember selectMember : selectMemberList) {
            List<StockDto.SelectStockManageMember> selectStockManageMemberList = stockRepository.selectStockManageAllByMember(MemberMapper.INSTANCE.selectMemberToMember(selectMember));

            StringBuffer stringBuffer = new StringBuffer();

            if (selectStockManageMemberList.size() > 0) {
                if (isMorning) {
                    stringBuffer.append("회원님의 관심 종목 개장 정보 🌞 \n \n");
                } else {
                    stringBuffer.append("회원님의 관심 종목 폐장 정보 🌝 \n \n");
                }

                for (StockDto.SelectStockManageMember stockManageMember : selectStockManageMemberList) {

                    JSONObject result = getStockInfo(stockManageMember.getStockCode());

                    StockDto.StockSearchInfo stockSearchInfo = StockDto.StockSearchInfo.builder()
                            .corpName(stockManageMember.getCorpName())
                            .price(decimalFormat.format(Long.parseLong(result.getString("stck_prpr"))))
                            .previousPrice(decimalFormat.format(Long.parseLong(result.getString("stck_sdpr"))))
                            .openPrice(decimalFormat.format(Long.parseLong(result.getString("stck_oprc"))))
                            .compareDayPoint(decimalFormat.format(Long.parseLong(result.getString("prdy_vrss"))))
                            .compareDayPercent(result.getString("prdy_ctrt") + "%")
                            .compareDaySign(result.getString("prdy_vrss_sign"))
                            .build();

                    if (StringUtils.hasText(stockSearchInfo.getCorpName())) {
                        stringBuffer.append("🟢 '")
                                .append(stockSearchInfo.getCorpName()).append("'의 정보 입니다. \n")
                                .append("현재 가격 : ").append(stockSearchInfo.getPrice())
                                .append("\n어제 종가 : ").append(stockSearchInfo.getPreviousPrice());

                        List<String> upList = new ArrayList<>(Arrays.asList("1", "2"));
                        List<String> downList = new ArrayList<>(Arrays.asList("4", "5"));

                        if (downList.contains(stockSearchInfo.getCompareDaySign())) {
                            stringBuffer.append("\n어제 대비 : ⬇️ ");
                        } else if (upList.contains(stockSearchInfo.getCompareDaySign())) {
                            stringBuffer.append("\n어제 대비 : ⬆️ ");
                        } else {
                            stringBuffer.append("\n어제 대비 : 🔁️ ");

                        }
                        stringBuffer.append(stockSearchInfo.getCompareDayPoint())
                                .append(" [")
                                .append(stockSearchInfo.getCompareDayPercent()).append("]\n \n");
                    } else {
                        stringBuffer.append("❌ '")
                                .append(stockManageMember.getCorpName()).append("'의 정보는 현재 제공 되지 않습니다.\n \n");
                    }

                }

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("message", stringBuffer.toString());

                messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
            }
        }
    }

    public String getStockApiToken() {
        String today = String.valueOf(LocalDate.now());

        StockApiInfo findStockApiInfo = stockApiInfoRepository.findByDate(today);

        if (findStockApiInfo == null) {
            String apiURL = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("grant_type", "client_credentials");
            jsonObject.put("appkey", appKey);
            jsonObject.put("appsecret", appSecret);

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // JSON 형식의 데이터를 전송할 것임을 지정

            RestTemplate restTemplate = new RestTemplate();

            // HTTP 요청 엔티티 생성
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);

            // POST 요청 보내기
            ResponseEntity<String> resultMap = restTemplate.postForEntity(apiURL, requestEntity, String.class);

            JSONObject result = new JSONObject(resultMap.getBody());


            StockApiInfo stockApiInfo = StockApiInfo.builder()
                    .token("Bearer " + result.getString("access_token"))
                    .issueDate(today)
                    .build();

            stockApiInfoRepository.save(stockApiInfo);

            return stockApiInfo.getToken();
        }

        return findStockApiInfo.getToken();

    }

    private JSONObject getStockInfo(String stockCode) {
        String apiURL = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=" + stockCode;    // json 결과

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", getStockApiToken());
        header.set("appkey", appKey);
        header.set("appsecret", appSecret);
        header.set("tr_id", trId);

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiURL).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(resultMap.getBody());

        return jsonObject.getJSONObject("output");
    }

}
