package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupByModelResolver<T, U> implements GroupFieldsResolver<U> {

    private final FieldResolver<T, U> fieldResolver;

    public List<U> resolve(FraudoParser.Group_byContext groupByContext) {
        List<U> fields = new ArrayList<>();
        if (groupByContext != null
                && groupByContext.string_list() != null
                && groupByContext.string_list().STRING() != null
                && !groupByContext.string_list().STRING().isEmpty()) {
            fields = groupByContext.string_list().STRING().stream()
                    .map(terminalNode -> fieldResolver.resolveName(TextUtil.safeGetText(terminalNode)))
                    .collect(Collectors.toList());
        }
        return fields;
    }

}
