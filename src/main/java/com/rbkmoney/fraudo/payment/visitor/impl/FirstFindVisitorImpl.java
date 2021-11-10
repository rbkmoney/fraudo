package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.FraudoPaymentBaseVisitor;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.converter.TrustConditionConverter;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.NotValidContextException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.model.RuleResult;
import com.rbkmoney.fraudo.model.TrustCondition;
import com.rbkmoney.fraudo.payment.generator.RuleKeyGenerator;
import com.rbkmoney.fraudo.payment.visitor.CountVisitor;
import com.rbkmoney.fraudo.payment.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.IsTrustedFuncVisitor;
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
    private final IsTrustedFuncVisitor<T> isTrustedFuncVisitor;
    private final FieldResolver<T, U> fieldResolver;
    private final TrustConditionConverter trustConditionConverter;

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
        List<RuleResult> results = new ArrayList<>();
        for (int i = 0; i < ctx.fraud_rule().size(); i++) {
            Fraud_ruleContext fraudRuleContext = ctx.fraud_rule().get(i);
            ResultStatus result = (ResultStatus) visit(fraudRuleContext);
            String key = RuleKeyGenerator.generateRuleKey(fraudRuleContext, i);
            if (result == null) {
                throw new UnknownResultException(fraudRuleContext.getText());
            } else if (result == ResultStatus.NOTIFY) {
                results.add(new RuleResult(result, key));
            } else if (result != ResultStatus.NORMAL) {
                results.add(new RuleResult(result, key));
                return new ResultModel(results);
            }
        }
        return new ResultModel(results);
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
        if (ctx.DECIMAL() != null) {
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
    public Double visitAmount(AmountContext ctx) {
        return Double.valueOf(threadLocalModel.get().getAmount());
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
        if (ctx.stringExpression().STRING() != null && ctx.stringExpression().getText() != null
                && !ctx.stringExpression().STRING().getText().isEmpty()) {
            String field = TextUtil.safeGetText(ctx.stringExpression().STRING());
            fieldValue = fieldResolver.resolve(field, threadLocalModel.get()).getSecond();
        } else {
            fieldValue = (String) visitChildren(ctx.stringExpression());
        }
        return ctx.string_list().STRING().stream()
                .anyMatch(s -> fieldValue.equals(TextUtil.safeGetText(s)));
    }

    @Override
    public Integer visitUnique(UniqueContext ctx) {
        String key = UniqueKeyGenerator.generate(ctx, fieldResolver::resolveName);
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
        String key = CountryKeyGenerator.generate(ctx);
        return (String) localFuncCache.get().computeIfAbsent(
                key,
                s -> customFuncVisitor.visitCountryBy(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Integer visitCount(CountContext ctx) {
        String key = CountKeyGenerator.generate(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCount(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Integer visitCount_success(Count_successContext ctx) {
        String key = CountKeyGenerator.generateSuccessKey(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCountSuccess(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Integer visitCount_error(Count_errorContext ctx) {
        String key = CountKeyGenerator.generateErrorKey(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCountError(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Integer visitCount_chargeback(Count_chargebackContext ctx) {
        String key = CountKeyGenerator.generateChargebackKey(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCountChargeback(ctx, threadLocalModel.get())
        );
    }

    @Override
    public Integer visitCount_refund(Count_refundContext ctx) {
        String key = CountKeyGenerator.generateRefundKey(ctx, fieldResolver::resolveName);
        return (Integer) localFuncCache.get().computeIfAbsent(
                key,
                s -> countVisitor.visitCountRefund(ctx, threadLocalModel.get())
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
    public Boolean visitIs_mobile(Is_mobileContext ctx) {
        return customFuncVisitor.visitCheckMobile(ctx, threadLocalModel.get());
    }

    @Override
    public Boolean visitIs_recurrent(Is_recurrentContext ctx) {
        return customFuncVisitor.visitCheckRecurrent(ctx, threadLocalModel.get());
    }

    @Override
    public Object visitIsTrusted(IsTrustedContext ctx) {
        return isTrustedFuncVisitor.visitCheckTrusted(threadLocalModel.get());
    }

    @Override
    public Object visitIsTrustedConditionsSingleList(IsTrustedConditionsSingleListContext ctx) {
        if (ctx.payment_conditions() != null && !ctx.payment_conditions().isEmpty()) {
            List<TrustCondition> paymentsConditions =
                    trustConditionConverter.convertToList(ctx.payment_conditions().conditions_list());
            return isTrustedFuncVisitor.visitCheckTrusted(paymentsConditions, null);
        }
        if (ctx.withdrawal_conditions() != null && !ctx.withdrawal_conditions().isEmpty()) {
            List<TrustCondition> withdrawalsConditions =
                    trustConditionConverter.convertToList(ctx.withdrawal_conditions().conditions_list());
            return isTrustedFuncVisitor.visitCheckTrusted(null, withdrawalsConditions);
        }
        throw new NotValidContextException();
    }

    @Override
    public Object visitIsTrustedPaymentsAndWithdrawalConditions(IsTrustedPaymentsAndWithdrawalConditionsContext ctx) {
        List<TrustCondition> paymentsConditions =
                trustConditionConverter.convertToList(ctx.payment_conditions().conditions_list());
        List<TrustCondition> withdrawalsConditions =
                trustConditionConverter.convertToList(ctx.withdrawal_conditions().conditions_list());
        return isTrustedFuncVisitor.visitCheckTrusted(paymentsConditions, withdrawalsConditions);
    }

}
