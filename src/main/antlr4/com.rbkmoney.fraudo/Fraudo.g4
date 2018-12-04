grammar Fraudo;

parse
 : fraud_rule* EOF
 ;

fraud_rule
 : RULE_BLOCK expression RETURN result SCOL
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
 | inWhiteList                                    #inWhiteListExpression
 | inBlackList                                    #inBlackListExpression
 | like                                           #likeFunctionExpression
 | equals_country                                 #equalsCountryFunctionExpression
 | IDENTIFIER                                     #identifierExpression
 | DECIMAL                                        #decimalExpression
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

inWhiteList
 : 'inWhiteList' LPAREN STRING RPAREN
 ;

inBlackList
 : 'inBlackList' LPAREN STRING RPAREN
 ;

like
 : 'like' LPAREN STRING DELIMETER STRING RPAREN
 ;

equals_country
 : 'equals_country' LPAREN RPAREN
 ;

DELIMETER : ',' ;

result
 : 'accept' | '3ds' | 'decline' | 'notify'
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

string_list
 : STRING (',' STRING | WS)+
 ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

RETURN     : '->' ;
RULE_BLOCK : 'rule:' ;
AND        : 'AND' ;
OR         : 'OR' ;
NOT        : 'NOT';
TRUE       : 'TRUE' ;
FALSE      : 'FALSE' ;
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