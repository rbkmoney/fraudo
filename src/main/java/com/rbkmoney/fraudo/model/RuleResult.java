package com.rbkmoney.fraudo.model;

import com.rbkmoney.fraudo.constant.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleResult {

    private ResultStatus resultStatus;
    private String ruleChecked;

}
