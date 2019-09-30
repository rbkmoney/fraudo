package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RuleKeyGenerator {

    public static String generateRuleKey(FraudoParser.Fraud_ruleContext fraudRuleContext) {
        if (fraudRuleContext.IDENTIFIER() != null && !fraudRuleContext.IDENTIFIER().getText().isEmpty()) {
            return fraudRuleContext.IDENTIFIER().getText();
        }
        return String.valueOf(fraudRuleContext.getRuleIndex());
    }

}
