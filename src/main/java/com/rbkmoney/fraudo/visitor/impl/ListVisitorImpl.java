package com.rbkmoney.fraudo.visitor.impl;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.finder.InNamingListFinder;
import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.visitor.ListVisitor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListVisitorImpl<T, U> implements ListVisitor<T> {

    private final InListFinder<T, U> blackListFinder;
    private final InListFinder<T, U> whiteListFinder;
    private final InListFinder<T, U> greyListFinder;

    private final InNamingListFinder<T, U> listFinderNew;

    private final FieldPairResolver<T, U> fieldPairResolver;

    @Override
    public Boolean visitInWhiteList(FraudoParser.In_white_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), whiteListFinder, model);
    }

    @Override
    public Boolean visitInBlackList(FraudoParser.In_black_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), blackListFinder, model);
    }

    @Override
    public Boolean visitInGreyList(FraudoParser.In_grey_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), greyListFinder, model);
    }

    @Override
    public Boolean visitInList(FraudoParser.In_listContext ctx, T model) {
        List<String> fields = ctx.string_list().STRING().stream()
                .map(TextUtil::safeGetText)
                .collect(Collectors.toList());
        List<Pair<U, String>> checkedFields = fields.stream()
                .map(s -> fieldPairResolver.resolve(s, model))
                .collect(Collectors.toList());
        String name = TextUtil.safeGetText(ctx.STRING());
        return listFinderNew.findInList(name, checkedFields, model);    }

    private Boolean findInList(List<TerminalNode> nodes, InListFinder listFinder, T model) {
        List<String> fields = nodes.stream()
                .map(TextUtil::safeGetText)
                .collect(Collectors.toList());
        List<Pair<U, String>> checkedFields = fields.stream()
                .map(s -> fieldPairResolver.resolve(s, model))
                .collect(Collectors.toList());
        return listFinder.findInList(checkedFields, model);
    }
}
