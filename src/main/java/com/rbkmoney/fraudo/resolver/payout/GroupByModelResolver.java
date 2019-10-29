package com.rbkmoney.fraudo.resolver.payout;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.GroupFieldsResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupByModelResolver<U> implements GroupFieldsResolver<U> {

    private final FieldNameResolver<U> payoutModelFieldNameResolver;

    public List<U> resolve(FraudoParser.Group_byContext groupByContext) {
        List<U> fields = new ArrayList<>();
        if (groupByContext != null
                && groupByContext.string_list() != null
                && groupByContext.string_list().STRING() != null
                && !groupByContext.string_list().STRING().isEmpty()) {
            fields = groupByContext.string_list().STRING().stream()
                    .map(terminalNode -> payoutModelFieldNameResolver.resolve(TextUtil.safeGetText(terminalNode)))
                    .collect(Collectors.toList());
        }
        return fields;
    }

}
