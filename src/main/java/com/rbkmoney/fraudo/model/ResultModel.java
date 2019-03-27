package com.rbkmoney.fraudo.model;

import com.rbkmoney.fraudo.constant.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultModel {

    private ResultStatus resultStatus;
    private String ruleChecked;
    private List<String> notificationsRule;
}
