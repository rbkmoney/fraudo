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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListVisitorImpl extends FraudoBaseVisitor<Object> {

    private final FraudModel fraudModel;
    private final InListFinder blackListFinder;
    private final InListFinder whiteListFinder;
    private final InListFinder greyListFinder;

    @Override
    public Object visitIn_white_list(FraudoParser.In_white_listContext ctx) {
        return findInList(ctx.string_list().STRING(), whiteListFinder);
    }

    @Override
    public Object visitIn_black_list(FraudoParser.In_black_listContext ctx) {
        return findInList(ctx.string_list().STRING(), blackListFinder);
    }

    @Override
    public Object visitIn_grey_list(FraudoParser.In_grey_listContext ctx) {
        return findInList(ctx.string_list().STRING(), greyListFinder);
    }
    
    private Object findInList(List<TerminalNode> nodes, InListFinder listFinder) {
        List<String> fields = nodes.stream()
                .map(TextUtil::safeGetText)
                .collect(Collectors.toList());
        List<String> values = fields.stream()
                .map(field -> FieldResolver.resolveString(field, fraudModel))
                .collect(Collectors.toList());
        List<CheckedField> checkedFields = fields.stream()
                .map(CheckedField::getByValue)
                .collect(Collectors.toList());
        return listFinder.findInList(fraudModel.getPartyId(), fraudModel.getShopId(),
                checkedFields, values);
    }
}
