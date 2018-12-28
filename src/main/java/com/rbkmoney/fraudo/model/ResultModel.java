package com.rbkmoney.fraudo.model;

import com.rbkmoney.fraudo.constant.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultModel {

    private ResultStatus resultStatus;
    private List<String> notificationsRule;
}
