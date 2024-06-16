package com.jung.notify.service;

import com.jung.notify.domain.AmericaStock;
import com.jung.notify.domain.AmericaStockManage;
import com.jung.notify.dto.AmericaStockDto;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.AmericaStockManageRepository;
import com.jung.notify.repository.AmericaStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AmericaStockService {

    private final AmericaStockRepository americaStockRepository;

    private final AmericaStockManageRepository americaStockManageRepository;

    private final MemberService memberService;

    private final StockService stockService;

    private final MessageService messageService;

    @Value("${korea.stock.appkey}")
    private String appKey;

    @Value("${korea.stock.appsecret}")
    private String appSecret;

    public Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockList(String searchText, Pageable pageable, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        return americaStockRepository.selectAmericaStockList(searchText, pageable, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
    }

    public boolean saveAmericaStockManage(Long stockId, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        AmericaStock americaStock = americaStockRepository.findById(stockId).orElseGet(null);

        if (americaStock == null) {
            return false;
        }

        AmericaStockManage americaStockManage = AmericaStockManage.builder()
                .americaStock(americaStock)
                .member(MemberMapper.INSTANCE.selectMemberToMember(selectMember))
                .build();

        americaStockManageRepository.save(americaStockManage);

        return true;
    }

    public Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockManageList(Pageable pageable, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        return americaStockManageRepository.selectAmericaStockManageList(pageable, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
    }

    public void sendAmericaStockPrice(boolean isMorning) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        List<MemberDto.SelectMember> selectMemberList = memberService.findAllMember();

        for (MemberDto.SelectMember selectMember : selectMemberList) {
            List<AmericaStockDto.SelectAmericaStockManageMember> selectAmericaStockManageMemberList =
                    americaStockRepository.selectAmericaStockManageAllByMember(MemberMapper.INSTANCE.selectMemberToMember(selectMember));

            StringBuffer stringBuffer = new StringBuffer();

            if (selectAmericaStockManageMemberList.size() > 0) {
                if (isMorning) {
                    stringBuffer.append("회원님의 관심 종목 폐장 정보 🌝 \n \n");
                } else {
                    stringBuffer.append("회원님의 관심 종목 개장 정보 🌞 \n \n");
                }
            }

            for (AmericaStockDto.SelectAmericaStockManageMember selectAmericaStockManageMember : selectAmericaStockManageMemberList) {
                String exchange = "";

                if ("NASDAQ".equals(selectAmericaStockManageMember.getExchange())) {
                    exchange = "NAS";
                } else if ("AMEX".equals(selectAmericaStockManageMember.getExchange())) {
                    exchange = "AMS";
                } else if ("NYSE".equals(selectAmericaStockManageMember.getExchange())) {
                    exchange = "NYS";
                }

                JSONObject result = getStockInfo(selectAmericaStockManageMember.getSymbol(), exchange);

                AmericaStockDto.AmericaStockSearchInfo americaStockSearchInfo = AmericaStockDto.AmericaStockSearchInfo.builder()
                        .corpName(selectAmericaStockManageMember.getKoreanName())
                        .price(result.getString("last"))
                        .previousPrice(result.getString("base"))
                        .openPrice(result.getString("last"))
                        .compareDayPoint(result.getString("diff"))
                        .compareDayPercent(result.getString("rate") + "%")
                        .compareDaySign(result.getString("sign"))
                        .build();

                if (StringUtils.hasText(americaStockSearchInfo.getCorpName())) {
                    stringBuffer.append("🟢 '")
                            .append(americaStockSearchInfo.getCorpName()).append("'의 정보 입니다. \n")
                            .append("현재 가격 : ").append(americaStockSearchInfo.getPrice()).append(" $")
                            .append("\n어제 종가 : ").append(americaStockSearchInfo.getPreviousPrice()).append(" $");

                    List<String> upList = new ArrayList<>(Arrays.asList("1", "2"));
                    List<String> downList = new ArrayList<>(Arrays.asList("4", "5"));

                    if (downList.contains(americaStockSearchInfo.getCompareDaySign())) {
                        stringBuffer.append("\n어제 대비 : ⬇️ ");
                    } else if (upList.contains(americaStockSearchInfo.getCompareDaySign())) {
                        stringBuffer.append("\n어제 대비 : ⬆️ ");
                    } else {
                        stringBuffer.append("\n어제 대비 : 🔁️ ");

                    }
                    stringBuffer.append(americaStockSearchInfo.getCompareDayPoint())
                            .append(" $ [")
                            .append(americaStockSearchInfo.getCompareDayPercent()).append("]\n \n");
                } else {
                    stringBuffer.append("❌ '")
                            .append(americaStockSearchInfo.getCorpName()).append("'의 정보는 현재 제공 되지 않습니다.\n \n");
                }
            }

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("message", stringBuffer.toString());

            messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
        }
    }

    public JSONObject getStockInfo(String symbol, String exchange) {
        String apiURL = "https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/price?" +
                "AUTH=&EXCD=" + exchange + "&SYMB=" + symbol;    // json 결과

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", stockService.getStockApiInfo().getToken());
        header.set("appkey", appKey);
        header.set("appsecret", appSecret);
        header.set("tr_id", "HHDFS00000300");

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiURL).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(resultMap.getBody());

        return jsonObject.getJSONObject("output");
    }
}
