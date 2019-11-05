package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.model.BaseModel;
import org.antlr.v4.runtime.tree.ParseTree;

public interface TemplateVisitor<T extends BaseModel> {

    Object visit(ParseTree tree, T model);

}
