package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.payment.visitor.ListVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

@RequiredArgsConstructor
public class ListVisitorImpl<T, U> implements ListVisitor<T> {

    private final InListFinder<T, U> listFinder;
    private final FieldResolver<T, U> fieldResolver;

    @Override
    public Boolean visitInWhiteList(In_white_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), model, listFinder::findInWhiteList);
    }

    @Override
    public Boolean visitInBlackList(In_black_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), model, listFinder::findInBlackList);
    }

    @Override
    public Boolean visitInGreyList(In_grey_listContext ctx, T model) {
        return findInList(ctx.string_list().STRING(), model, listFinder::findInGreyList);
    }

    @Override
    public Boolean visitInList(In_listContext ctx, T model) {
        List<Pair<U, String>> checkedFields = initCheckedFields(model, ctx.string_list().STRING());
        String name = TextUtil.safeGetText(ctx.STRING());
        return listFinder.findInList(name, checkedFields, model);
    }

    private List<Pair<U, String>> initCheckedFields(T model, List<TerminalNode> string) {
        List<String> fields = string.stream()
                .map(TextUtil::safeGetText)
                .collect(Collectors.toList());
        return fields.stream()
                .map(s -> fieldResolver.resolve(s, model))
                .collect(Collectors.toList());
    }

    private Boolean findInList(List<TerminalNode> nodes, T model, BiPredicate<List<Pair<U, String>>, T> biPredicate) {
        List<Pair<U, String>> checkedFields = initCheckedFields(model, nodes);
        return biPredicate.test(checkedFields, model);
    }
}
