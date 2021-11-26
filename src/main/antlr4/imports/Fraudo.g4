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

payment_system
 : 'paymentSystem' LPAREN RPAREN
 ;

card_category
 : 'cardCategory' LPAREN RPAREN
 ;

payer_type
 : 'payerType' LPAREN RPAREN
 ;

token_provider
 : 'tokenProvider' LPAREN RPAREN
 ;

count
 : 'count' LPAREN STRING time_window (group_by)? RPAREN
 ;

sum
 : 'sum' LPAREN STRING time_window (group_by)? RPAREN
 ;

unique
 : 'unique' LPAREN STRING DELIMITER STRING time_window (group_by)? RPAREN
 ;

in
 : 'in' LPAREN (country_by | STRING) DELIMITER string_list RPAREN
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
 : 'inList' LPAREN STRING DELIMITER string_list RPAREN
 ;

like
 : 'like' LPAREN STRING DELIMITER STRING RPAREN
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
 : STRING (DELIMITER STRING | WS)*
 ;

time_window
 : DELIMITER INTEGER | DELIMITER INTEGER DELIMITER INTEGER
 ;

group_by
 : DELIMITER string_list
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

DELIMITER : ',' ;

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
WS         : [ \u000C\t\n]+ -> skip;
SCOL       : ';';
BOOLEAN    : TRUE | FALSE;
