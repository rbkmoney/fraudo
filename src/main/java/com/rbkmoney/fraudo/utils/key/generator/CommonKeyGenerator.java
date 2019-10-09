package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonKeyGenerator {

    static String generateKeyGroupedFunction(TerminalNode string,
                                             ParseTree parseTree,
                                             FraudoParser.Time_windowContext timeWindowContext,
                                             FraudoParser.Group_byContext groupByContext) {
        String countTarget = TextUtil.safeGetText(string);
        return new StringBuilder()
                .append(parseTree)
                .append(countTarget)
                .append(CheckedField.getByValue(countTarget))
                .append(timeWindowContext != null ? timeWindowContext.children : "")
                .append(groupByContext != null ? groupByContext.string_list().children : "")
                .toString();
    }

    static String generateKeyGroupedTwoFieldFunction(TerminalNode firstField,
                                                     TerminalNode secondField,
                                                     ParseTree parseTree,
                                                     FraudoParser.Time_windowContext timeWindowContext,
                                                     FraudoParser.Group_byContext groupByContext) {
        String target = TextUtil.safeGetText(firstField);
        String errorCode = TextUtil.safeGetText(secondField);
        return new StringBuilder()
                .append(parseTree)
                .append(target)
                .append(errorCode)
                .append(CheckedField.getByValue(target))
                .append(timeWindowContext != null ? timeWindowContext.children : "")
                .append(groupByContext != null ? groupByContext.string_list().children : "")
                .toString();
    }

}
