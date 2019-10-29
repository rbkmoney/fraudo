grammar Fraudo;

parse
 : fraud_rule* EOF
 ;

fraud_rule
 : RULE_BLOCK (IDENTIFIER ':')? expression RETURN result (CATCH_ERROR catch_result)? SCOL
 ;

expression
 : LPAREN expression RPAREN                       #parenExpression
 | NOT expression                                 #notExpression
 | left=expression op=comparator right=expression #comparatorExpression
 | left=expression op=binary right=expression     #binaryExpression
 | bool                                           #boolExpression
 | count                                          #countExpression
 | count_success                                  #countSuccessExpression
 | count_error                                    #countErrorExpression
 | sum                                            #sumExpression
 | sum_success                                    #sumSuccessExpression
 | sum_error                                      #sumErrorExpression
 | unique                                         #uniqueExpression
 | in                                             #inFunctionExpression
 | in_white_list                                  #inWhiteListExpression
 | in_black_list                                  #inBlackListExpression
 | in_grey_list                                   #inGreyListExpression
 | like                                           #likeFunctionExpression
 | country                                        #countryFunctionExpression
 | country_by                                     #countryByFunctionExpression
 | amount                                         #amountFunctionExpression
 | currency                                       #currencyFunctionExpression
 | IDENTIFIER                                     #identifierExpression
 | DECIMAL                                        #decimalExpression
 | STRING                                         #stringExpression
 ;

comparator
 : GT | GE | LT | LE | EQ
 ;

binary
 : AND | OR
 ;

bool
 : TRUE | FALSE
 ;

amount
 : 'amount' LPAREN RPAREN
 ;

currency
 : 'currency' LPAREN RPAREN
 ;

count
 : 'count' LPAREN STRING time_window (group_by)? RPAREN
 ;

count_success
 : 'countSuccess' LPAREN STRING time_window (group_by)? RPAREN
 ;

count_error
 : 'countError' LPAREN STRING time_window DELIMETER STRING (group_by)? RPAREN
 ;

sum
 : 'sum' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_success
 : 'sumSuccess' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum_error
 : 'sumError' LPAREN STRING time_window DELIMETER STRING (group_by)? RPAREN
 ;

unique
 : 'unique' LPAREN STRING DELIMETER STRING time_window (group_by)? RPAREN
 ;

in
 : 'in' LPAREN (country_by | STRING) DELIMETER string_list RPAREN
 ;

in_white_list
 : 'inWhiteList' LPAREN string_list RPAREN
 ;

in_black_list
 : 'inBlackList' LPAREN string_list RPAREN
 ;

in_grey_list
 : 'inGreyList' LPAREN string_list RPAREN
 ;

like
 : 'like' LPAREN STRING DELIMETER STRING RPAREN
 ;

country
 : 'country' LPAREN RPAREN
 ;

country_by
 : 'countryBy' LPAREN STRING RPAREN
 ;

result
 : 'accept' | '3ds' | 'highRisk' | 'decline' | 'notify'
 ;

catch_result
 : 'accept' | '3ds' | 'highRisk' | 'notify'
 ;

string_list
 : STRING (DELIMETER STRING | WS)*
 ;

time_window
 : DELIMETER DECIMAL | DELIMETER DECIMAL DELIMETER DECIMAL
 ;

group_by
 : DELIMETER string_list
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

DELIMETER : ',' ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

RETURN     : '->' ;
CATCH_ERROR: 'catch:' ;
RULE_BLOCK : 'rule:' ;
AND        : 'AND' | 'and';
OR         : 'OR' | 'or' ;
NOT        : 'NOT' | 'not';
TRUE       : 'TRUE' | 'true';
FALSE      : 'FALSE' | 'false';
GT         : '>' ;
GE         : '>=' ;
LT         : '<' ;
LE         : '<=' ;
EQ         : '=' ;
LPAREN     : '(' ;
RPAREN     : ')' ;
DECIMAL    : '-'? [0-9]+ ( '.' [0-9]+ )? ;
IDENTIFIER : [a-zA-Z_] [a-zA-Z_0-9]* ;
WS         : [ \u000C\n]+ -> skip;
SCOL       : ';';