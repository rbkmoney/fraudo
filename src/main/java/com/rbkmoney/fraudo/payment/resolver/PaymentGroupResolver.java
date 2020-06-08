package com.rbkmoney.fraudo.payment.resolver;

import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupFieldsResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.fraudo.FraudoPaymentParser.Group_byContext;

@RequiredArgsConstructor
public class PaymentGroupResolver<T, U> implements GroupFieldsResolver<Group_byContext, U> {

    private final FieldResolver<T, U> fieldResolver;

    public List<U> resolve(Group_byContext groupByContext) {
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
