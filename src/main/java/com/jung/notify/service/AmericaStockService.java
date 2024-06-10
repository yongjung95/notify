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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AmericaStockService {

    private final AmericaStockRepository americaStockRepository;

    private final AmericaStockManageRepository americaStockManageRepository;

    private final MemberService memberService;

    public Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockList(String koreanName, Pageable pageable, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId);

        return americaStockRepository.selectAmericaStockList(koreanName, pageable, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
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

}
