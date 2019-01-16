package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

@RequiredArgsConstructor
public class ListVisitorImpl extends FraudoBaseVisitor<Object> {

    private final FraudModel fraudModel;
    private final InListFinder blackListFinder;
    private final InListFinder whiteListFinder;

    @Override
    public Object visitIn_white_list(FraudoParser.In_white_listContext ctx) {
        return findInList(ctx.STRING(), whiteListFinder);
    }

    @Override
    public Object visitIn_black_list(FraudoParser.In_black_listContext ctx) {
        return findInList(ctx.STRING(), blackListFinder);
    }

    private Object findInList(TerminalNode string, InListFinder blackListFinder) {
        String fieldName = TextUtil.safeGetText(string);
        String fieldValue = FieldResolver.resolveString(fieldName, fraudModel);
        return blackListFinder.findInList(fraudModel.getPartyId(), fraudModel.getShopId(),
                CheckedField.getByValue(fieldName), fieldValue);
    }
}
