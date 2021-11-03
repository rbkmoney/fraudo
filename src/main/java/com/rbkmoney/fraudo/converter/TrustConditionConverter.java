package com.rbkmoney.fraudo.converter;

import com.rbkmoney.fraudo.FraudoPaymentParser;
import com.rbkmoney.fraudo.model.TrustCondition;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.fraudo.utils.TextUtil.safeGetInteger;
import static com.rbkmoney.fraudo.utils.TextUtil.safeGetText;

@RequiredArgsConstructor
public class TrustConditionConverter {

    private static final int YEARS_OFFSET_INDEX = 0;
    private static final int COUNT_INDEX = 1;
    private static final int SUM_INDEX = 2;

    public List<TrustCondition> convertToList(FraudoPaymentParser.Conditions_listContext conditionsListContext) {
        return conditionsListContext.trusted_token_condition().stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public TrustCondition convert(FraudoPaymentParser.Trusted_token_conditionContext ctx) {
        List<TerminalNode> intNodes = ctx.INTEGER();
        return TrustCondition.builder()
                .transactionsCurrency(safeGetText(ctx.STRING()))
                .transactionsYearsOffset(safeGetInteger(intNodes.get(YEARS_OFFSET_INDEX)))
                .transactionsCount(safeGetInteger(intNodes.get(COUNT_INDEX)))
                .transactionsSum(
                        hasSumArgument(intNodes.size()) ? safeGetInteger(intNodes.get(SUM_INDEX)) : null)
                .build();
    }

    private boolean hasSumArgument(int intNodesSize) {
        return intNodesSize > SUM_INDEX;
    }

}
