package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.NotValidContextException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.payment.generator.RuleKeyGenerator;
import com.rbkmoney.fraudo.payment.visitor.*;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.utils.key.generator.CountKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.CountryKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.SumKeyGenerator;
import com.rbkmoney.fraudo.utils.key.generator.UniqueKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FirstFindVisitorImpl<T extends BaseModel, U> extends FraudoBaseVisitor<Object> implements TemplateVisitor<T> {

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
    public Object visit(ParseTree tree, T model) {
        try {
            validateModel(model);
            threadLocalModel.set(model);
            return super.visit(tree);
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
    public Object visitFraud_rule(FraudoParser.Fraud_ruleContext ctx) {
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
    public Object visitParse(FraudoParser.ParseContext ctx) {
        List<String> notifications = new ArrayList<>();
        for (FraudoParser.Fraud_ruleContext fraudRuleContext : ctx.fraud_rule()) {
            ResultStatus result = (ResultStatus) visitFraud_rule(fraudRuleContext);
            String key = RuleKeyGenerator.generateRuleKey(fraudRuleContext);
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
    public Object visitResult(FraudoParser.ResultContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitDecimalExpression(FraudoParser.DecimalExpressionContext ctx) {
        return Double.valueOf(ctx.DECIMAL().getText());
    }

    @Override
    public Object visitStringExpression(FraudoParser.StringExpressionContext ctx) {
        return TextUtil.safeGetText(ctx.STRING());
    }

    @Override
    public Object visitNotExpression(FraudoParser.NotExpressionContext ctx) {
        return !((Boolean) this.visit(ctx.expression()));
    }

    @Override
    public Object visitParenExpression(FraudoParser.ParenExpressionContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public Object visitComparatorExpression(FraudoParser.ComparatorExpressionContext ctx) {
        if (ctx.op.EQ() != null) {
            return this.visit(ctx.left).equals(this.visit(ctx.right));
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
    public Object visitBinaryExpression(FraudoParser.BinaryExpressionContext ctx) {
        if (ctx.op.AND() != null) {
            return asBoolean(ctx.left) && asBoolean(ctx.right);
        } else if (ctx.op.OR() != null) {
            return asBoolean(ctx.left) || asBoolean(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Object visitBoolExpression(FraudoParser.BoolExpressionContext ctx) {
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Object visitCount(FraudoParser.CountContext ctx) {
        String key = CountKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCount(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitCount_success(FraudoParser.Count_successContext ctx) {
        String key = CountKeyGenerator.generateSuccessKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountSuccess(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitCount_error(FraudoParser.Count_errorContext ctx) {
        String key = CountKeyGenerator.generateErrorKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountError(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitCount_chargeback(FraudoParser.Count_chargebackContext ctx) {
        String key = CountKeyGenerator.generateChargebackKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountChargeback(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitCount_refund(FraudoParser.Count_refundContext ctx) {
        String key = CountKeyGenerator.generateRefundKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCountRefund(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitSum(FraudoParser.SumContext ctx) {
        String key = SumKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSum(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitSum_success(FraudoParser.Sum_successContext ctx) {
        String key = SumKeyGenerator.generateSuccessKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumSuccess(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitSum_error(FraudoParser.Sum_errorContext ctx) {
        String key = SumKeyGenerator.generateErrorKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumError(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitSum_chargeback(FraudoParser.Sum_chargebackContext ctx) {
        String key = SumKeyGenerator.generateChargebackKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumChargeback(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitSum_refund(FraudoParser.Sum_refundContext ctx) {
        String key = SumKeyGenerator.generateRefundKey(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSumRefund(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitCountry_by(FraudoParser.Country_byContext ctx) {
        String key = CountryKeyGenerator.generate(ctx);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitIn(FraudoParser.InContext ctx) {
        return customFuncVisitor.visitIn(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitLike(FraudoParser.LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitUnique(FraudoParser.UniqueContext ctx) {
        String key = UniqueKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(customFuncVisitor.visitUnique(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitIn_white_list(FraudoParser.In_white_listContext ctx) {
        return listVisitor.visitInWhiteList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_black_list(FraudoParser.In_black_listContext ctx) {
        return listVisitor.visitInBlackList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_grey_list(FraudoParser.In_grey_listContext ctx) {
        return listVisitor.visitInGreyList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_list(FraudoParser.In_listContext ctx) {
        return listVisitor.visitInList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitAmount(FraudoParser.AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
    }

    @Override
    public Object visitCurrency(FraudoParser.CurrencyContext ctx) {
        return threadLocalModel.get().getCurrency();
    }

    private boolean asBoolean(FraudoParser.ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(FraudoParser.ExpressionContext ctx) {
        return (double) visit(ctx);
    }
}
