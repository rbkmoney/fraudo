package com.rbkmoney.fraudo.p2p.visitor.impl;

import com.rbkmoney.fraudo.FraudoP2PBaseVisitor;
import com.rbkmoney.fraudo.FraudoP2PParser.*;
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
    public ResultStatus visitFraud_rule(Fraud_ruleContext ctx) {
        try {
            if (visitExpression(ctx.expression())) {
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

    @Override
    public ResultModel visitParse(ParseContext ctx) {
        List<String> notifications = new ArrayList<>();
        for (int i = 0; i < ctx.fraud_rule().size(); i++) {
            Fraud_ruleContext fraudRuleContext = ctx.fraud_rule().get(i);
            ResultStatus result = (ResultStatus) visit(fraudRuleContext);
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
    public Boolean visitExpression(ExpressionContext ctx) {
        if (ctx.OR() != null && !ctx.OR().isEmpty()) {
            boolean result = false;
            for (BooleanAndExpressionContext booleanAndExpressionContext : ctx.booleanAndExpression()) {
                result = result || (Boolean) visit(booleanAndExpressionContext);
                if (result) {
                    return result;
                }
            }
            return result;
        }
        return visitBooleanAndExpression(ctx.booleanAndExpression(0));
    }

    @Override
    public Boolean visitBooleanAndExpression(BooleanAndExpressionContext ctx) {
        if (ctx.AND() != null && !ctx.AND().isEmpty()) {
            boolean result = true;
            for (EqualityExpressionContext equalityExpressionContext : ctx.equalityExpression()) {
                result = result && visitEqualityExpression(equalityExpressionContext);
                if (!result) {
                    return result;
                }
            }
            return result;
        }
        return visitEqualityExpression(ctx.equalityExpression(0));
    }

    @Override
    public Boolean visitEqualityExpression(EqualityExpressionContext ctx) {
        if (ctx.stringExpression() != null && !ctx.stringExpression().isEmpty()) {
            String left = visitStringExpression(ctx.stringExpression(0));
            String right = visitStringExpression(ctx.stringExpression(1));
            if (ctx.EQ() != null) {
                return left.equals(right);
            } else if (ctx.NEQ() != null) {
                return !left.equals(right);
            }
        } else if (ctx.NOT() != null) {
            return !(Boolean) visit(ctx.expression());
        } else if (ctx.LPAREN() != null) {
            return (Boolean) visit(ctx.expression());
        }
        return visitRelationalExpression(ctx.relationalExpression());
    }

    @Override
    public Boolean visitRelationalExpression(RelationalExpressionContext ctx) {

        if (ctx.unaryExpression() != null && !ctx.unaryExpression().isEmpty()) {
            Double left = visitUnaryExpression(ctx.unaryExpression(0));
            Double right = visitUnaryExpression(ctx.unaryExpression(1));
            if (ctx.EQ() != null) {
                return left.equals(right);
            } else if (ctx.NEQ() != null) {
                return !left.equals(right);
            } else if (ctx.GE() != null) {
                return left >= right;
            } else if (ctx.LT() != null) {
                return left < right;
            } else if (ctx.GT() != null) {
                return left > right;
            } else if (ctx.LE() != null) {
                return left <= right;
            }
        }
        return (Boolean) visitChildren(ctx);
    }

    @Override
    public Double visitUnaryExpression(UnaryExpressionContext ctx) {
        if (ctx.floatExpression() != null) {
            return visitFloatExpression(ctx.floatExpression());
        } else if (ctx.integerExpression() != null) {
            return Double.valueOf(visitIntegerExpression(ctx.integerExpression()));
        }
        throw new NotImplementedOperatorException(ctx.getText());
    }

    @Override
    public Double visitFloatExpression(FloatExpressionContext ctx) {
        if (ctx.INTEGER() != null) {
            return Double.valueOf(TextUtil.safeGetText(ctx.INTEGER()));
        } else if (ctx.DECIMAL() != null) {
            return Double.valueOf(TextUtil.safeGetText(ctx.DECIMAL()));
        }
        return (Double) visitChildren(ctx);
    }

    @Override
    public Integer visitIntegerExpression(IntegerExpressionContext ctx) {
        if (ctx.INTEGER() != null) {
            return Integer.valueOf(TextUtil.safeGetText(ctx.INTEGER()));
        }
        return (Integer) visitChildren(ctx);
    }

    @Override
    public String visitStringExpression(StringExpressionContext ctx) {
        if (ctx.STRING() != null) {
            return TextUtil.safeGetText(ctx.STRING());
        }
        return (String) visitChildren(ctx);
    }

    @Override
    public Integer visitCount(CountContext ctx) {
        String key = CountP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCount(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Double visitAmount(AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
    }

    @Override
    public Double visitSum(SumContext ctx) {
        String key = SumP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Double) localFuncCache.get().computeIfAbsent(
                key,
                s -> sumVisitor.visitSum(ctx, threadLocalModel.get())
        );
    }

    @Override
    public String visitCurrency(CurrencyContext ctx) {
        return threadLocalModel.get().getCurrency();
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
    public Boolean visitIn(InContext ctx) {
        String fieldValue;
        if (ctx.STRING() != null) {
            String field = TextUtil.safeGetText(ctx.STRING());
            fieldValue = fieldResolver.resolve(field, threadLocalModel.get()).getSecond();
        } else {
            fieldValue = visitCountry_by(ctx.country_by());
        }
        return ctx.string_list().STRING().stream()
                .anyMatch(s -> fieldValue.equals(TextUtil.safeGetText(s)));
    }

    @Override
    public Integer visitUnique(UniqueContext ctx) {
        String key = UniqueP2PKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitUnique(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Boolean visitLike(LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx, threadLocalModel.get());
    }

    @Override
    public String visitCountry_by(Country_byContext ctx) {
        String key = CountryP2PKeyGenerator.generate(ctx);
        return (String) localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }
}
