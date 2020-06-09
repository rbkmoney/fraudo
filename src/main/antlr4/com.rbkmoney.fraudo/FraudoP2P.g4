grammar FraudoP2P;

import Fraudo;

//This rule override for run test in idea plugin
parse
 : fraud_rule* EOF
 ;

expression
    :   booleanAndExpression ( OR booleanAndExpression )*
    ;

booleanAndExpression
    :    equalityExpression ( AND equalityExpression )*
    ;

equalityExpression
    :    relationalExpression
    |    stringExpression ( (EQ | NEQ) stringExpression)?
    |    NOT expression
    |    LPAREN expression RPAREN
    ;

stringExpression
    :   country
    |   country_by
    |   currency
    |   STRING
    ;

relationalExpression
    : unaryExpression (LT | LE | GT | GE | EQ | NEQ) unaryExpression
    | in
    | in_white_list
    | in_black_list
    | in_grey_list
    | in_list
    | like
    ;

unaryExpression
    :    integerExpression
    |    floatExpression
    ;

integerExpression
    :   count
    |   unique
    |   INTEGER
    ;

floatExpression
    :   sum
    |   amount
    |   INTEGER
    |   DECIMAL
    ;