package com.jung.notify.domain;

import lombok.*;

import javax.persistence.*;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AmericaStockManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private AmericaStock americaStock;

    private boolean isUse;

    public void changeIsUse() {
        isUse = !isUse;
    }
}
