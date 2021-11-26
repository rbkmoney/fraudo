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
    | payment_system
    | card_category
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
    | is_mobile
    | is_recurrent
    | is_trusted
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
 : 'countError' LPAREN STRING time_window DELIMITER STRING (group_by)? RPAREN
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
 : 'sumError' LPAREN STRING time_window DELIMITER STRING (group_by)? RPAREN
 ;

sum_chargeback
 : 'sumChargeback' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_refund
 : 'sumRefund' LPAREN STRING time_window (group_by)? RPAREN
 ;

in
 : 'in' LPAREN (stringExpression) DELIMITER string_list RPAREN
 ;

is_mobile
 : 'isMobile' LPAREN RPAREN
 ;

is_recurrent
 : 'isRecurrent' LPAREN RPAREN
 ;

is_trusted
 : 'isTrusted' LPAREN RPAREN                                                        #isTrusted
 | 'isTrusted' LPAREN STRING RPAREN                                                 #isTrustedTemplateName
 | 'isTrusted' LPAREN (payment_conditions | withdrawal_conditions) RPAREN           #isTrustedConditionsSingleList
 | 'isTrusted' LPAREN payment_conditions DELIMITER withdrawal_conditions RPAREN     #isTrustedPaymentsAndWithdrawalConditions
 ;

payment_conditions
 : 'paymentsConditions' LPAREN conditions_list RPAREN
 ;

withdrawal_conditions
 : 'withdrawalsConditions' LPAREN conditions_list RPAREN
 ;

conditions_list
 : trusted_token_condition (DELIMITER trusted_token_condition | WS)*
 ;

trusted_token_condition
 : 'condition' LPAREN
        STRING DELIMITER            //transactions_currency
        INTEGER DELIMITER           //transactions_years_offset
        INTEGER DELIMITER           //transactions_count
        INTEGER                     //transactions_sum
    RPAREN
 | 'condition' LPAREN
        STRING DELIMITER            //transactions_currency
        INTEGER DELIMITER           //transactions_years_offset
        INTEGER                     //transactions_count
    RPAREN
 ;
