grammar Fraudo;

parse
 : fraud_rule* EOF
 ;

fraud_rule
 : RULE_BLOCK expression RETURN result (CATCH_ERROR catch_result)? SCOL
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
 | like                                           #likeFunctionExpression
 | country                                        #countryFunctionExpression
 | amount                                         #amountFunctionExpression
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

count
 : 'count' LPAREN STRING DELIMETER DECIMAL RPAREN
 ;

count_success
 : 'countSuccess' LPAREN STRING DELIMETER DECIMAL RPAREN
 ;

count_error
 : 'countError' LPAREN STRING DELIMETER DECIMAL DELIMETER STRING RPAREN
 ;

sum
 : 'sum' LPAREN STRING DELIMETER DECIMAL RPAREN
 ;

sum_success
 : 'sumSuccess' LPAREN STRING DELIMETER DECIMAL RPAREN
 ;

sum_error
 : 'sumError' LPAREN STRING DELIMETER DECIMAL DELIMETER STRING RPAREN
 ;

unique
 : 'unique' LPAREN STRING DELIMETER STRING RPAREN
 ;

in
 : 'in' LPAREN STRING DELIMETER string_list RPAREN
 ;

in_white_list
 : 'inWhiteList' LPAREN STRING RPAREN
 ;

in_black_list
 : 'inBlackList' LPAREN STRING RPAREN
 ;

like
 : 'like' LPAREN STRING DELIMETER STRING RPAREN
 ;

country
  : 'country' LPAREN RPAREN
  ;

result
 : 'accept' | '3ds' | 'decline' | 'notify'
 ;

catch_result
 : 'accept' | '3ds' | 'decline' | 'notify'
 ;

string_list
 : STRING (',' STRING | WS)+
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