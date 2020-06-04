package com.rbkmoney.fraudo.p2p.visitor.impl;

import com.rbkmoney.fraudo.FraudoP2PBaseVisitor;
import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.NotValidContextException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.p2p.generator.*;
import com.rbkmoney.fraudo.p2p.visitor.CountP2PVisitor;
import com.rbkmoney.fraudo.p2p.visitor.CustomP2PFuncVisitor;
import com.rbkmoney.fraudo.p2p.visitor.ListP2PVisitor;
import com.rbkmoney.fraudo.p2p.visitor.SumP2PVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.visitor.TemplateVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FirstFindP2PVisitorImpl<T extends BaseModel, U> extends FraudoP2PBaseVisitor<Object> implements TemplateVisitor<T, ResultModel> {

    private ThreadLocal<Map<String, Object>> localFuncCache = ThreadLocal.withInitial(HashMap::new);
    private ThreadLocal<T> threadLocalModel = new ThreadLocal<>();

    private final CountP2PVisitor<T> countVisitor;
    private final SumP2PVisitor<T> sumVisitor;
    private final ListP2PVisitor<T> listVisitor;
    private final CustomP2PFuncVisitor<T> customFuncVisitor;
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
            return (ResultModel) visit(tree);
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
    public Object visitFraud_rule(FraudoP2PParser.Fraud_ruleContext ctx) {
        try {
            if (asBoolean(ctx.expression())) {
                return ResultStatus.getByValue((String) visit(ctx.result()));
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
    public Object visitParse(FraudoP2PParser.ParseContext ctx) {
        List<String> notifications = new ArrayList<>();
        for (FraudoP2PParser.Fraud_ruleContext fraudRuleContext : ctx.fraud_rule()) {
            ResultStatus result = (ResultStatus) visitFraud_rule(fraudRuleContext);
            String key = RuleP2PKeyGenerator.generateRuleKey(fraudRuleContext);
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
    public Object visitResult(FraudoP2PParser.ResultContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitDecimalExpression(FraudoP2PParser.DecimalExpressionContext ctx) {
        return Double.valueOf(ctx.DECIMAL().getText());
    }

    @Override
    public Object visitStringExpression(FraudoP2PParser.StringExpressionContext ctx) {
        return TextUtil.safeGetText(ctx.STRING());
    }

    @Override
    public Object visitNotExpression(FraudoP2PParser.NotExpressionContext ctx) {
        return !((Boolean) visit(ctx.expression()));
    }

    @Override
    public Object visitParenExpression(FraudoP2PParser.ParenExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitComparatorExpression(FraudoP2PParser.ComparatorExpressionContext ctx) {
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
    public Object visitBinaryExpression(FraudoP2PParser.BinaryExpressionContext ctx) {
        if (ctx.op.AND() != null) {
            return asBoolean(ctx.left) && asBoolean(ctx.right);
        } else if (ctx.op.OR() != null) {
            return asBoolean(ctx.left) || asBoolean(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Object visitBoolExpression(FraudoP2PParser.BoolExpressionContext ctx) {
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Object visitCount(FraudoP2PParser.CountContext ctx) {
        String key = CountP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(countVisitor.visitCount(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitSum(FraudoP2PParser.SumContext ctx) {
        String key = SumP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSum(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitCountry_by(FraudoP2PParser.Country_byContext ctx) {
        String key = CountryP2PKeyGenerator.generate(ctx);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitIn(FraudoP2PParser.InContext ctx) {
        return customFuncVisitor.visitIn(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitLike(FraudoP2PParser.LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitUnique(FraudoP2PParser.UniqueContext ctx) {
        String key = UniqueP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(customFuncVisitor.visitUnique(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitIn_white_list(FraudoP2PParser.In_white_listContext ctx) {
        return listVisitor.visitInWhiteList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_black_list(FraudoP2PParser.In_black_listContext ctx) {
        return listVisitor.visitInBlackList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_grey_list(FraudoP2PParser.In_grey_listContext ctx) {
        return listVisitor.visitInGreyList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_list(FraudoP2PParser.In_listContext ctx) {
        return listVisitor.visitInList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitAmount(FraudoP2PParser.AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
    }

    @Override
    public Object visitCurrency(FraudoP2PParser.CurrencyContext ctx) {
        return threadLocalModel.get().getCurrency();
    }

    private boolean asBoolean(FraudoP2PParser.ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(FraudoP2PParser.ExpressionContext ctx) {
        return (double) visit(ctx);
    }
}
