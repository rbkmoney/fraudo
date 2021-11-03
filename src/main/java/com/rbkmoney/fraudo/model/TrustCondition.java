package com.rbkmoney.fraudo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrustCondition {

    private String transactionsCurrency;
    private Integer transactionsYearsOffset;
    private Integer transactionsSum;
    private Integer transactionsCount;

}
