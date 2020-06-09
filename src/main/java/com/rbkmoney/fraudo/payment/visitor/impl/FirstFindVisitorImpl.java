package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.FraudoPaymentBaseVisitor;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.NotValidContextException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.payment.generator.RuleKeyGenerator;
import com.rbkmoney.fraudo.payment.visitor.CountVisitor;
import com.rbkmoney.fraudo.payment.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.ListVisitor;
import com.rbkmoney.fraudo.payment.visitor.SumVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.utils.key.generator.CountKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.CountryKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.SumKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.UniqueKeyGenerator;
import com.rbkmoney.fraudo.visitor.TemplateVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

@Slf4j
@RequiredArgsConstructor
public class FirstFindVisitorImpl<T extends BaseModel, U> extends FraudoPaymentBaseVisitor<Object> implements TemplateVisitor<T, ResultModel> {

    private ThreadLocal<Map<String, Object>> localFuncCache = ThreadLocal.withInitial(HashMap::new);
    private ThreadLocal<T> threadLocalModel = new ThreadLocal<>();

    private final CountVisitor<T> countVisitor;
    private final SumVisitor<T> sumVisitor;
    private final ListVisitor<T> listVisitor;
    private final CustomFuncVisitor<T> customFuncVisitor;
    private final FieldResolver<T, U> fieldResolver;

    @Override
    public void close() {
        localFuncCache.remove();
        threadLocalModel.remove();
    }

    @Override
    public ResultModel visit(ParseTree tree, T model) {
        try {
            validateModel(model);
            threadLocalModel.set(model);
            return (ResultModel) super.visit(tree);
        } finally {
            localFuncCache.get().clear();
        }
    }

    private void validateModel(T model) {
        if (model == null) {
            log.error("Model is not init!");
            throw new NotValidContextException();
        }
    }

    @Override
    public ResultStatus visitFraud_rule(Fraud_ruleContext ctx) {
        try {
            if (asBoolean(ctx.expression())) {
                return ResultStatus.getByValue((String) super.visit(ctx.result()));
            }
        } catch (Exception e) {
            log.error("Error when FastFraudVisitorImpl visitFraud_rule e: ", e);
            if (ctx.catch_result() != null && ctx.catch_result().getText() != null) {
                return ResultStatus.getByValue(ctx.catch_result().getText());
            }
            return ResultStatus.NOTIFY;
        }
        return ResultStatus.NORMAL;
    }

    @Override
    public ResultModel visitParse(ParseContext ctx) {
        List<String> notifications = new ArrayList<>();
        for (int i = 0; i < ctx.fraud_rule().size(); i++) {
            Fraud_ruleContext fraudRuleContext = ctx.fraud_rule().get(i);
            ResultStatus result = visitFraud_rule(fraudRuleContext);
            String key = RuleKeyGenerator.generateRuleKey(fraudRuleContext, i);
            if (result == null) {
                throw new UnknownResultException(fraudRuleContext.getText());
            } else if (result == ResultStatus.NOTIFY) {
                notifications.add(key);
            } else if (result != ResultStatus.NORMAL) {
                return new ResultModel(result, key, notifications);
            }
        }
        return new ResultModel(ResultStatus.NORMAL, notifications);
    }

    @Override
    public String visitResult(ResultContext ctx) {
        return ctx.getText();
    }

    @Override
    public Double visitDecimalExpression(DecimalExpressionContext ctx) {
        return Double.valueOf(ctx.DECIMAL().getText());
    }

    @Override
    public Double visitIntegerExpression(IntegerExpressionContext ctx) {
        return Double.valueOf(ctx.INTEGER().getText());
    }

    @Override
    public String visitStringExpression(StringExpressionContext ctx) {
        return TextUtil.safeGetText(ctx.STRING());
    }

    @Override
    public Boolean visitNotExpression(NotExpressionContext ctx) {
        return !((Boolean) visit(ctx.expression()));
    }

    @Override
    public Object visitParenExpression(ParenExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Boolean visitComparatorExpression(ComparatorExpressionContext ctx) {
        if (ctx.op.EQ() != null) {
            return visit(ctx.left).equals(visit(ctx.right));
        } else if (ctx.op.LE() != null) {
            return asDouble(ctx.left) <= asDouble(ctx.right);
        } else if (ctx.op.GE() != null) {
            return asDouble(ctx.left) >= asDouble(ctx.right);
        } else if (ctx.op.LT() != null) {
            return asDouble(ctx.left) < asDouble(ctx.right);
        } else if (ctx.op.GT() != null) {
            return asDouble(ctx.left) > asDouble(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Boolean visitBinaryExpression(BinaryExpressionContext ctx) {
        if (ctx.op.AND() != null) {
            return asBoolean(ctx.left) && asBoolean(ctx.right);
        } else if (ctx.op.OR() != null) {
            return asBoolean(ctx.left) || asBoolean(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Boolean visitBoolExpression(BoolExpressionContext ctx) {
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Double visitCount(CountContext ctx) {
        String key = CountKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCount(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Double visitCount_success(Count_successContext ctx) {
        String key = CountKeyGenerator.generateSuccessKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountSuccess(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Double visitCount_error(Count_errorContext ctx) {
        String key = CountKeyGenerator.generateErrorKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountError(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Double visitCount_chargeback(Count_chargebackContext ctx) {
        String key = CountKeyGenerator.generateChargebackKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountChargeback(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Double visitCount_refund(Count_refundContext ctx) {
        String key = CountKeyGenerator.generateRefundKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountRefund(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Double visitSum(SumContext ctx) {
        String key = SumKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSum(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Double visitSum_success(Sum_successContext ctx) {
        String key = SumKeyGenerator.generateSuccessKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumSuccess(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Double visitSum_error(Sum_errorContext ctx) {
        String key = SumKeyGenerator.generateErrorKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumError(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Double visitSum_chargeback(Sum_chargebackContext ctx) {
        String key = SumKeyGenerator.generateChargebackKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumChargeback(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Double visitSum_refund(Sum_refundContext ctx) {
        String key = SumKeyGenerator.generateRefundKey(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumRefund(ctx, threadLocalModel.get())
        );
    }

    @Override
    public String visitCountry_by(Country_byContext ctx) {
        String key = CountryKeyGenerator.generate(ctx);
        return (String) localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Boolean visitIn(InContext ctx) {
        return customFuncVisitor.visitIn(ctx, threadLocalModel.get());
    }

    @Override
    public Boolean visitLike(LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx, threadLocalModel.get());
    }

    @Override
    public Double visitUnique(UniqueContext ctx) {
        String key = UniqueKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(customFuncVisitor.visitUnique(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Boolean visitIn_white_list(In_white_listContext ctx) {
        return listVisitor.visitInWhiteList(ctx, threadLocalModel.get());
    }

    @Override
    public Boolean visitIn_black_list(In_black_listContext ctx) {
        return listVisitor.visitInBlackList(ctx, threadLocalModel.get());
    }

    @Override
    public Boolean visitIn_grey_list(In_grey_listContext ctx) {
        return listVisitor.visitInGreyList(ctx, threadLocalModel.get());
    }

    @Override
    public Boolean visitIn_list(In_listContext ctx) {
        return listVisitor.visitInList(ctx, threadLocalModel.get());
    }

    @Override
    public Double visitAmount(AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
    }

    @Override
    public String visitCurrency(CurrencyContext ctx) {
        return threadLocalModel.get().getCurrency();
    }

    private boolean asBoolean(ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(ExpressionContext ctx) {
        return (double) visit(ctx);
    }
}
