package com.rbkmoney.fraudo.p2p.visitor.impl;

import com.rbkmoney.fraudo.FraudoP2PBaseVisitor;
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

import static com.rbkmoney.fraudo.FraudoP2PParser.*;

@Slf4j
@RequiredArgsConstructor
public class FirstFindP2PVisitorImpl<T extends BaseModel, U> extends FraudoP2PBaseVisitor<Object> implements TemplateVisitor<T, ResultModel> {

    private CountCustomVisitor countCustomVisitor = new CountCustomVisitor();
    private SumCustomVisitor sumCustomVisitor = new SumCustomVisitor();
    private FraudRuleCustomVisitor fraudRuleCustomVisitor = new FraudRuleCustomVisitor();
    private ParseRuleCustomVisitor parseRuleCustomVisitor = new ParseRuleCustomVisitor();

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
            return parseRuleCustomVisitor.visit(tree);
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
    public Object visitDecimalExpression(DecimalExpressionContext ctx) {
        return Double.valueOf(ctx.DECIMAL().getText());
    }

    @Override
    public Object visitStringExpression(StringExpressionContext ctx) {
        return TextUtil.safeGetText(ctx.STRING());
    }

    @Override
    public Object visitNotExpression(NotExpressionContext ctx) {
        return !((Boolean) visit(ctx.expression()));
    }

    @Override
    public Object visitParenExpression(ParenExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitComparatorExpression(ComparatorExpressionContext ctx) {
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
    public Object visitBinaryExpression(BinaryExpressionContext ctx) {
        if (ctx.op.AND() != null) {
            return asBoolean(ctx.left) && asBoolean(ctx.right);
        } else if (ctx.op.OR() != null) {
            return asBoolean(ctx.left) || asBoolean(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Object visitBoolExpression(BoolExpressionContext ctx) {
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Object visitCount(CountContext ctx) {
        return countCustomVisitor.visitCount(ctx);
    }

    @Override
    public Object visitSum(SumContext ctx) {
        return sumCustomVisitor.visitSum(ctx);
    }

    @Override
    public Object visitCountry_by(Country_byContext ctx) {
        String key = CountryP2PKeyGenerator.generate(ctx);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Object visitIn(InContext ctx) {
        return customFuncVisitor.visitIn(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitLike(LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitUnique(UniqueContext ctx) {
        String key = UniqueP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return localFuncCache.get().computeIfAbsent(
                key,
                s -> Double.valueOf(customFuncVisitor.visitUnique(ctx, threadLocalModel.get()))
        );
    }

    @Override
    public Object visitIn_white_list(In_white_listContext ctx) {
        return listVisitor.visitInWhiteList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_black_list(In_black_listContext ctx) {
        return listVisitor.visitInBlackList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_grey_list(In_grey_listContext ctx) {
        return listVisitor.visitInGreyList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIn_list(In_listContext ctx) {
        return listVisitor.visitInList(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitAmount(AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
    }

    @Override
    public Object visitCurrency(CurrencyContext ctx) {
        return threadLocalModel.get().getCurrency();
    }

    private boolean asBoolean(ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(ExpressionContext ctx) {
        return (double) visit(ctx);
    }

    public class CountCustomVisitor extends FraudoP2PBaseVisitor<Double> {

        private ThreadLocal<Map<String, Double>> localFuncCache = ThreadLocal.withInitial(HashMap::new);

        @Override
        public Double visitCount(CountContext ctx) {
            String key = CountP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
            return localFuncCache.get().computeIfAbsent(
                    key,
                    s -> Double.valueOf(countVisitor.visitCount(ctx, threadLocalModel.get()))
            );
        }

    }

    public class SumCustomVisitor extends FraudoP2PBaseVisitor<Double> {

        private ThreadLocal<Map<String, Double>> localFuncCache = ThreadLocal.withInitial(HashMap::new);

        @Override
        public Double visitSum(SumContext ctx) {
            String key = SumP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
            return localFuncCache.get().computeIfAbsent(
                    key,
                    s -> sumVisitor.visitSum(ctx, threadLocalModel.get())
            );
        }

    }

    public class FraudRuleCustomVisitor extends FraudoP2PBaseVisitor<ResultStatus> {

        @Override
        public ResultStatus visitFraud_rule(Fraud_ruleContext ctx) {
            try {
                if (asBoolean(ctx.expression())) {
                    return ResultStatus.getByValue(ctx.result().getText());
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

    }

    public class ParseRuleCustomVisitor extends FraudoP2PBaseVisitor<ResultModel> {

        @Override
        public ResultModel visitParse(ParseContext ctx) {
            List<String> notifications = new ArrayList<>();
            for (int i = 0; i < ctx.fraud_rule().size(); i++) {
                Fraud_ruleContext fraudRuleContext = ctx.fraud_rule().get(i);
                ResultStatus result = fraudRuleCustomVisitor.visitFraud_rule(fraudRuleContext);
                String key = RuleP2PKeyGenerator.generateRuleKey(fraudRuleContext, i);
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

    }

}
