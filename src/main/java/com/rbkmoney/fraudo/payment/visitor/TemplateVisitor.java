package com.rbkmoney.fraudo.payment.visitor;

import com.rbkmoney.fraudo.model.BaseModel;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.Closeable;

public interface TemplateVisitor<T extends BaseModel> extends Closeable {

    Object visit(ParseTree tree, T model);

}
