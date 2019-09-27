package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.model.TimeWindow;
import com.rbkmoney.fraudo.utils.TextUtil;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class TimeWindowResolver {

    public static TimeWindow resolve(FraudoParser.Time_windowContext ctx) {
        TimeWindow.TimeWindowBuilder builder = TimeWindow.builder();
        List<TerminalNode> times = ctx.DECIMAL();
        String startWindow = TextUtil.safeGetText(times.get(0));
        if (times.size() == 2) {
            String endWindow = TextUtil.safeGetText(times.get(1));
            builder.endWindowTime(Long.valueOf(endWindow));
        }
        return builder
                .startWindowTime(Long.valueOf(startWindow))
                .build();
    }

}
