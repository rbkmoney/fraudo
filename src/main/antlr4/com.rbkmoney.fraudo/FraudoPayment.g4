grammar FraudoPayment;

import Fraudo;

//This rule override for run test in idea plugin
parse
 : fraud_rule* EOF
 ;

expression
    : booleanAndExpression ( OR booleanAndExpression )*
    ;

booleanAndExpression
    : equalityExpression ( AND equalityExpression )*
    ;

equalityExpression
    : relationalExpression
    | stringExpression (EQ | NEQ) stringExpression
    | NOT expression
    | LPAREN expression RPAREN
    ;

stringExpression
    : country_by
    | currency
    | STRING
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
    : integerExpression
    | floatExpression
    ;

integerExpression
    : count
    | count_success
    | count_error
    | count_chargeback
    | count_refund
    | unique
    | INTEGER
    ;

floatExpression
    : sum
    | sum_success
    | sum_error
    | sum_chargeback
    | sum_refund
    | amount
    | DECIMAL
    ;

count_success
 : 'countSuccess' LPAREN STRING time_window (group_by)? RPAREN
 ;

count_error
 : 'countError' LPAREN STRING time_window DELIMETER STRING (group_by)? RPAREN
 ;

count_chargeback
 : 'countChargeback' LPAREN STRING time_window (group_by)? RPAREN
 ;

count_refund
 : 'countRefund' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_success
 : 'sumSuccess' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_error
 : 'sumError' LPAREN STRING time_window DELIMETER STRING (group_by)? RPAREN
 ;

sum_chargeback
 : 'sumChargeback' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_refund
 : 'sumRefund' LPAREN STRING time_window (group_by)? RPAREN
 ;

in
 : 'in' LPAREN (stringExpression) DELIMETER string_list RPAREN
 ;