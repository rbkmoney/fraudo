package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.utils.RuleKeyGenerator;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FastFraudVisitorImpl extends FraudoBaseVisitor<Object> {

    private final CountVisitorImpl countVisitor;
    private final SumVisitorImpl sumVisitor;
    private final ListVisitorImpl listVisitor;
    private final CustomFuncVisitorImpl customFuncVisitor;

    @Override
    public Object visitFraud_rule(FraudoParser.Fraud_ruleContext ctx) {
        try {
            if (asBoolean(ctx.expression())) {
                return ResultStatus.getByValue((String) super.visit(ctx.result()));
            }
        } catch (Exception e) {
            if (ctx.catch_result() != null && ctx.catch_result().getText() != null) {
                return ResultStatus.getByValue(ctx.catch_result().getText());
            }
            return ResultStatus.THREE_DS;
        }
        return ResultStatus.NORMAL;
    }

    @Override
    public Object visitParse(FraudoParser.ParseContext ctx) {
        List<String> notifications = new ArrayList<>();
        for (FraudoParser.Fraud_ruleContext fraudRuleContext : ctx.fraud_rule()) {
            ResultStatus result = (ResultStatus) visitFraud_rule(fraudRuleContext);
            String key = RuleKeyGenerator.generateRuleKey(fraudRuleContext);
            if (result != null && ResultStatus.NOTIFY.equals(result)) {
                notifications.add(key);
            } else if (result != null && !ResultStatus.NORMAL.equals(result)) {
                return new ResultModel(result, key, notifications);
            } else if (result == null) {
                throw new UnknownResultException(fraudRuleContext.getText());
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
        return countVisitor.visitCount(ctx);
    }

    @Override
    public Object visitCount_success(FraudoParser.Count_successContext ctx) {
        return countVisitor.visitCount_success(ctx);
    }

    @Override
    public Object visitCount_error(FraudoParser.Count_errorContext ctx) {
        return countVisitor.visitCount_error(ctx);
    }

    @Override
    public Object visitSum(FraudoParser.SumContext ctx) {
        return sumVisitor.visitSum(ctx);
    }

    @Override
    public Object visitSum_success(FraudoParser.Sum_successContext ctx) {
        return sumVisitor.visitSum_success(ctx);
    }

    @Override
    public Object visitSum_error(FraudoParser.Sum_errorContext ctx) {
        return sumVisitor.visitSum_error(ctx);
    }

    @Override
    public Object visitCountry_by(FraudoParser.Country_byContext ctx) {
        return customFuncVisitor.visitCountry_by(ctx);
    }

    @Override
    public Object visitIn(FraudoParser.InContext ctx) {
        return customFuncVisitor.visitIn(ctx);
    }

    @Override
    public Object visitLike(FraudoParser.LikeContext ctx) {
        return customFuncVisitor.visitLike(ctx);
    }

    @Override
    public Object visitUnique(FraudoParser.UniqueContext ctx) {
        return customFuncVisitor.visitUnique(ctx);
    }

    @Override
    public Object visitIn_white_list(FraudoParser.In_white_listContext ctx) {
        return listVisitor.visitIn_white_list(ctx);
    }

    @Override
    public Object visitIn_black_list(FraudoParser.In_black_listContext ctx) {
        return listVisitor.visitIn_black_list(ctx);
    }

    @Override
    public Object visitAmount(FraudoParser.AmountContext ctx) {
        return customFuncVisitor.visitAmount(ctx);
    }

    private boolean asBoolean(FraudoParser.ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(FraudoParser.ExpressionContext ctx) {
        return (double) visit(ctx);
    }
}
