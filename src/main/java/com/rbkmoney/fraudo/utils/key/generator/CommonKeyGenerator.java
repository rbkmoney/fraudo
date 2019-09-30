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

    static String generateKeyGroupedFunction(TerminalNode string, ParseTree parseTree, FraudoParser.Time_windowContext time_windowContext, FraudoParser.Group_byContext group_byContext) {
        String countTarget = TextUtil.safeGetText(string);
        return new StringBuilder()
                .append(parseTree)
                .append(countTarget)
                .append(CheckedField.getByValue(countTarget))
                .append(time_windowContext != null ? time_windowContext.children : "")
                .append(group_byContext != null ? group_byContext.string_list().children : "")
                .toString();
    }

    static String generateKeyGroupedErrorFunction(TerminalNode string, TerminalNode string2, ParseTree parseTree, FraudoParser.Time_windowContext time_windowContext, FraudoParser.Group_byContext group_byContext) {
        String countTarget = TextUtil.safeGetText(string);
        String errorCode = TextUtil.safeGetText(string2);
        return new StringBuilder()
                .append(parseTree)
                .append(countTarget)
                .append(errorCode)
                .append(CheckedField.getByValue(countTarget))
                .append(time_windowContext != null ? time_windowContext.children : "")
                .append(group_byContext != null ? group_byContext.string_list().children : "")
                .toString();
    }

}
