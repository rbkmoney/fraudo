package com.rbkmoney.fraudo.utils;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.model.RuleResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResultUtils {

    public static List<String> getNotifications(ResultModel result) {
        return result.getRuleResults().stream()
                .filter(ruleResult -> ResultStatus.NOTIFY.equals(ruleResult.getResultStatus())
                        || ResultStatus.DECLINE_AND_NOTIFY.equals(ruleResult.getResultStatus())
                        || ResultStatus.ACCEPT_AND_NOTIFY.equals(ruleResult.getResultStatus()))
                .map(RuleResult::getRuleChecked)
                .collect(Collectors.toList());
    }

    public static Optional<RuleResult> findFirstNotNotifyStatus(ResultModel result) {
        return result.getRuleResults().stream()
                .filter(ruleResult -> !ruleResult.getResultStatus().equals(ResultStatus.NOTIFY))
                .findFirst();
    }

}
