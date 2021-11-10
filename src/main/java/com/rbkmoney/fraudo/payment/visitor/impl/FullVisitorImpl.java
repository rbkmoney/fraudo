package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.converter.TrustConditionConverter;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.model.RuleResult;
import com.rbkmoney.fraudo.payment.generator.RuleKeyGenerator;
import com.rbkmoney.fraudo.payment.visitor.CountVisitor;
import com.rbkmoney.fraudo.payment.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.IsTrustedFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.ListVisitor;
import com.rbkmoney.fraudo.payment.visitor.SumVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.rbkmoney.fraudo.FraudoPaymentParser.Fraud_ruleContext;
import static com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;

@Slf4j
public class FullVisitorImpl<T extends BaseModel, U> extends FirstFindVisitorImpl<T, U> {

    public FullVisitorImpl(
            CountVisitor<T> countVisitor,
            SumVisitor<T> sumVisitor,
            ListVisitor<T> listVisitor,
            CustomFuncVisitor<T> customFuncVisitor,
            IsTrustedFuncVisitor<T> isTrustedFuncVisitor,
            FieldResolver<T, U> fieldResolver,
            TrustConditionConverter trustConditionConverter
    ) {
        super(countVisitor, sumVisitor, listVisitor, customFuncVisitor, isTrustedFuncVisitor, fieldResolver,
                trustConditionConverter);
    }

    @Override
    public ResultModel visitParse(ParseContext ctx) {
        List<RuleResult> results = new ArrayList<>();
        for (int i = 0; i < ctx.fraud_rule().size(); i++) {
            Fraud_ruleContext fraudRuleContext = ctx.fraud_rule().get(i);
            ResultStatus result = (ResultStatus) visit(fraudRuleContext);
            String key = RuleKeyGenerator.generateRuleKey(fraudRuleContext, i);
            if (result == null) {
                throw new UnknownResultException(fraudRuleContext.getText());
            } else if (result != ResultStatus.NORMAL) {
                results.add(new RuleResult(result, key));
            }
        }
        return new ResultModel(results);
    }

}
