package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.NotImplementedOperatorException;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FastFraudVisitorImpl extends FraudoBaseVisitor<Object> {

    private final CountVisitorImpl countVisitor;
    private final SumVisitorImpl sumVisitor;
    private final ListVisitorImpl listVisitor;
    private final CustomFuncVisitorImpl customFuncVisitor;

    @Override
    public Object visitFraud_rule(com.rbkmoney.fraudo.FraudoParser.Fraud_ruleContext ctx) {
        if (asBoolean(ctx.expression())) {
            return super.visit(ctx.result());
        }
        return ResultStatus.ACCEPT;
    }

    @Override
    public Object visitParse(com.rbkmoney.fraudo.FraudoParser.ParseContext ctx) {
        for (com.rbkmoney.fraudo.FraudoParser.Fraud_ruleContext fraud_ruleContext : ctx.fraud_rule()) {
            if (asBoolean(fraud_ruleContext.expression())) {
                String result = fraud_ruleContext.result().getText();
                return Optional.ofNullable(ResultStatus.getByValue(result))
                        .orElseThrow(() -> new UnknownResultException(result));
            }
        }
        return ResultStatus.ACCEPT;
    }

    @Override
    public Object visitResult(com.rbkmoney.fraudo.FraudoParser.ResultContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitDecimalExpression(com.rbkmoney.fraudo.FraudoParser.DecimalExpressionContext ctx) {
        return Double.valueOf(ctx.DECIMAL().getText());
    }

    @Override
    public Object visitStringExpression(FraudoParser.StringExpressionContext ctx) {
        return TextUtil.safeGetText(ctx.STRING());
    }

    @Override
    public Object visitNotExpression(com.rbkmoney.fraudo.FraudoParser.NotExpressionContext ctx) {
        return !((Boolean) this.visit(ctx.expression()));
    }

    @Override
    public Object visitParenExpression(com.rbkmoney.fraudo.FraudoParser.ParenExpressionContext ctx) {
        return super.visit(ctx.expression());
    }

    @Override
    public Object visitComparatorExpression(com.rbkmoney.fraudo.FraudoParser.ComparatorExpressionContext ctx) {
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
    public Object visitBinaryExpression(com.rbkmoney.fraudo.FraudoParser.BinaryExpressionContext ctx) {
        if (ctx.op.AND() != null) {
            return asBoolean(ctx.left) && asBoolean(ctx.right);
        } else if (ctx.op.OR() != null) {
            return asBoolean(ctx.left) || asBoolean(ctx.right);
        }
        throw new NotImplementedOperatorException(ctx.op.getText());
    }

    @Override
    public Object visitBoolExpression(com.rbkmoney.fraudo.FraudoParser.BoolExpressionContext ctx) {
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Object visitCount(com.rbkmoney.fraudo.FraudoParser.CountContext ctx) {
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

    private boolean asBoolean(com.rbkmoney.fraudo.FraudoParser.ExpressionContext ctx) {
        return (boolean) visit(ctx);
    }

    private double asDouble(com.rbkmoney.fraudo.FraudoParser.ExpressionContext ctx) {
        return (double) visit(ctx);
    }
}
