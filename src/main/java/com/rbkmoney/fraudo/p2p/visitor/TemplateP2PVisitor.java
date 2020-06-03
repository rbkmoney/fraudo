package com.rbkmoney.fraudo.p2p.visitor;

import com.rbkmoney.fraudo.model.BaseModel;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.Closeable;

public interface TemplateP2PVisitor<T extends BaseModel> extends Closeable {

    Object visit(ParseTree tree, T model);

}
