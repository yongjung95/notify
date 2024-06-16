package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.AmericaStockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AmericaStockRepositoryQuerydsl {

    Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockList(String searchText, Pageable pageable, Member searchMember);

    List<AmericaStockDto.SelectAmericaStockManageMember> selectAmericaStockManageAllByMember(Member member);
}
