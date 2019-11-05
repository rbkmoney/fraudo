package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonKeyGenerator {

    static <T> String generateKeyGroupedFunction(TerminalNode string,
                                                 ParseTree parseTree,
                                                 FraudoParser.Time_windowContext timeWindowContext,
                                                 FraudoParser.Group_byContext groupByContext,
                                                 Function<String, T> resolve) {
        String countTarget = TextUtil.safeGetText(string);
        return new StringBuilder()
                .append(parseTree)
                .append(countTarget)
                .append(resolve.apply(countTarget))
                .append(timeWindowContext != null ? timeWindowContext.children : "")
                .append(groupByContext != null ? groupByContext.string_list().children : "")
                .toString();
    }

    static <T> String generateKeyGroupedTwoFieldFunction(TerminalNode firstField,
                                                         TerminalNode secondField,
                                                         ParseTree parseTree,
                                                         FraudoParser.Time_windowContext timeWindowContext,
                                                         FraudoParser.Group_byContext groupByContext,
                                                         Function<String, T> resolve) {
        String target = TextUtil.safeGetText(firstField);
        String errorCode = TextUtil.safeGetText(secondField);
        return new StringBuilder()
                .append(parseTree)
                .append(target)
                .append(errorCode)
                .append(resolve.apply(target))
                .append(timeWindowContext != null ? timeWindowContext.children : "")
                .append(groupByContext != null ? groupByContext.string_list().children : "")
                .toString();
    }

}
