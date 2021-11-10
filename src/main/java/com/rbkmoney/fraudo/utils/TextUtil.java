package com.rbkmoney.fraudo.utils;

import com.rbkmoney.fraudo.exception.FieldUnsetException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextUtil {

    public static String safeGetText(TerminalNode field) {
        return Optional.ofNullable(field)
                .orElseThrow(FieldUnsetException::new)
                .getText().replace("\"", "");
    }

    public static Integer safeGetInteger(TerminalNode field) {
        return Optional.ofNullable(field)
                .map(node -> Integer.parseInt(node.getText()))
                .orElseThrow(FieldUnsetException::new);
    }

}
