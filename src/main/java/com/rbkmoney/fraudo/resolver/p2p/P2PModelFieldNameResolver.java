package com.rbkmoney.fraudo.resolver.p2p;

import com.rbkmoney.fraudo.constant.P2PCheckedField;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class P2PModelFieldNameResolver implements FieldNameResolver<P2PCheckedField> {

    @Override
    public P2PCheckedField resolve(String fieldName) {
        return P2PCheckedField.getByValue(fieldName);
    }

}
