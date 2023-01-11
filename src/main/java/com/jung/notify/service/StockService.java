package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.StockDto;
import com.jung.notify.repository.StockManageRepository;
import com.jung.notify.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.IOException;
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

    public Page<StockDto.SelectStock> selectStockList(String corpName, Pageable pageable, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return stockRepository.selectStockList(corpName, pageable, member);
    }

    public void saveStockManage(Long stockId, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        Stock stock = stockRepository.findById(stockId).orElseThrow(NullPointerException::new);

        StockManage stockManage = StockManage.builder()
                .stock(stock)
                .member(member)
                .build();

        stockManageRepository.save(stockManage);
    }

    public Page<StockDto.SelectStock> selectStockManageList(Pageable pageable, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return stockManageRepository.selectStockManageList(pageable, member);
    }

    public void sendMorningStockPriceList(boolean isMorning) {

        List<Member> memberList = memberService.findAllMember();

        for (Member member : memberList) {
            List<StockDto.SelectStockManageMember> selectStockManageMemberList = stockRepository.selectStockManageAllByMember(member);

            StringBuffer stringBuffer = new StringBuffer();

            if (selectStockManageMemberList.size() > 0) {
                if (isMorning) {
                    stringBuffer.append("회원님의 관심 종목 개장 정보 🌞 \n \n");
                }else {
                    stringBuffer.append("회원님의 관심 종목 폐장 정보 🌝 \n \n");
                }

                for (StockDto.SelectStockManageMember stockManageMember : selectStockManageMemberList) {

                    String url = "https://finance.naver.com/item/main.naver?code=" + stockManageMember.getStockCode();
                    Connection connection = Jsoup.connect(url);

                    try {
                        Document document = connection.get();

                        StockDto.StockSearchInfo stockSearchInfo = StockDto.StockSearchInfo.builder()
                                .corpName(document.select("div.wrap_company h2 a").text())
                                .price(document.select("p.no_today em span").select(".blind").text())
                                .previousPrice(document.select("#chart_area > div.rate_info > table > tbody > tr:nth-child(1) > td.first > em").select(".blind").text())
                                .openPrice(document.select("#chart_area > div.rate_info > table > tbody > tr:nth-child(2) > td.first > em").select(".blind").text())
                                .compareDayPoint(document.select("#chart_area > div.rate_info > dl.blind > dd:nth-child(3)").text())
                                .compareDayPercent(document.select("#chart_area > div.rate_info > dl.blind > dd:nth-child(4)").text())
                                .build();

                        if (StringUtils.hasText(stockSearchInfo.getCorpName())) {
                            stringBuffer.append("🟢 '")
                                    .append(stockSearchInfo.getCorpName()).append("'의 정보 입니다. \n")
                                    .append("현재 가격 : ").append(stockSearchInfo.getPrice())
                                    .append("\n어제 종가 : ").append(stockSearchInfo.getPreviousPrice());
                            if (stockSearchInfo.getCompareDayPoint().contains("하락")) {
                                stringBuffer.append("\n어제 대비 : ⬇️ ");
                            } else if (stockSearchInfo.getCompareDayPoint().contains("상승")) {
                                stringBuffer.append("\n어제 대비 : ⬆️ ");
                            } else {
                                stringBuffer.append("\n어제 대비 : 🔁️ ");

                            }
                            stringBuffer.append(stockSearchInfo.getCompareDayPoint())
                                    .append(" ")
                                    .append(stockSearchInfo.getCompareDayPercent()).append("\n \n");
                        } else {
                            stringBuffer.append("❌ '")
                                    .append(stockManageMember.getCorpName()).append("'의 정보는 현재 제공 되지 않습니다.\n \n");
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("message", stringBuffer.toString());

                messageService.sendMessage(body, member);
            }
        }
    }
}
