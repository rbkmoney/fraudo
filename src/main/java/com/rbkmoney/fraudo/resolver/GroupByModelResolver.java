package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupByModelResolver {

    public static List<CheckedField> resolve(FraudoParser.Group_byContext groupByContext) {
        List<CheckedField> checkedFields = new ArrayList<>();
        if (groupByContext != null
                && groupByContext.string_list() != null
                && groupByContext.string_list().STRING() != null
                && !groupByContext.string_list().STRING().isEmpty()) {
            checkedFields = groupByContext.string_list().STRING().stream()
                    .map(terminalNode -> CheckedField.getByValue(TextUtil.safeGetText(terminalNode)))
                    .collect(Collectors.toList());
        }
        return checkedFields;
    }

}
