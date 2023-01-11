package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.StockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockRepositoryQuerydsl {

    Page<StockDto.SelectStock> selectStockList(String corpName, Pageable pageable, Member searchMember);

    List<StockDto.SelectStockManageMember> selectStockManageAllByMember(Member member);
}
