package com.rbkmoney.fraudo.p2p.generator;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonP2PKeyGenerator {

    static <T> String generateKeyGroupedFunction(TerminalNode string,
                                                 ParseTree parseTree,
                                                 FraudoP2PParser.Time_windowContext timeWindowContext,
                                                 FraudoP2PParser.Group_byContext groupByContext,
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
                                                         FraudoP2PParser.Time_windowContext timeWindowContext,
                                                         FraudoP2PParser.Group_byContext groupByContext,
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
