package com.rbkmoney.fraudo.p2p.generator;

import com.rbkmoney.fraudo.FraudoP2PParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RuleP2PKeyGenerator {

    public static String generateRuleKey(FraudoP2PParser.Fraud_ruleContext fraudRuleContext) {
        if (fraudRuleContext.IDENTIFIER() != null && !fraudRuleContext.IDENTIFIER().getText().isEmpty()) {
            return fraudRuleContext.IDENTIFIER().getText();
        }
        return String.valueOf(fraudRuleContext.getRuleIndex());
    }

}
