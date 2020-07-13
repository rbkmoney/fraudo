grammar Fraudo;

parse
 : fraud_rule* EOF
 ;

fraud_rule
 : RULE_BLOCK (IDENTIFIER ':')? expression RETURN result (CATCH_ERROR catch_result)? SCOL
 ;

expression
 : LPAREN expression RPAREN  #parenExpression
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

sum
 : 'sum' LPAREN STRING time_window (group_by)? RPAREN
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

in_list
 : 'inList' LPAREN STRING DELIMETER string_list RPAREN
 ;

like
 : 'like' LPAREN STRING DELIMETER STRING RPAREN
 ;

country_by
 : 'countryBy' LPAREN STRING RPAREN
 ;

result
 : 'accept'
 | '3ds'
 | 'highRisk'
 | 'decline'
 | 'notify'
 | 'declineAndNotify'
 | 'acceptAndNotify'
 ;

catch_result
 : 'accept'
 | '3ds'
 | 'highRisk'
 | 'notify'
 ;

string_list
 : STRING (DELIMETER STRING | WS)*
 ;

time_window
 : DELIMETER INTEGER | DELIMETER INTEGER DELIMETER INTEGER
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
NEQ        : '!=' ;
LPAREN     : '(' ;
RPAREN     : ')' ;
DECIMAL    : '-'? ('0'..'9')+ '.' ('0'..'9')+;
INTEGER    : '-'? ('0'..'9')+;
IDENTIFIER : [a-zA-Z_] [a-zA-Z_0-9]* ;
WS         : [ \u000C\n]+ -> skip;
SCOL       : ';';
BOOLEAN    : TRUE | FALSE;