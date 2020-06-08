grammar FraudoP2P;

import Fraudo;

//This rule override for run test in idea plugin
parse
 : fraud_rule* EOF
 ;

expression
 : LPAREN expression RPAREN                       #parenExpression
 | NOT expression                                 #notExpression
 | left=expression op=comparator right=expression #comparatorExpression
 | left=expression op=binary right=expression     #binaryExpression
 | bool                                           #boolExpression
 | count                                          #countExpression
 | sum                                            #sumExpression
 | unique                                         #uniqueExpression
 | in                                             #inFunctionExpression
 | in_white_list                                  #inWhiteListExpression
 | in_black_list                                  #inBlackListExpression
 | in_grey_list                                   #inGreyListExpression
 | in_list                                        #inListExpression
 | like                                           #likeFunctionExpression
 | country                                        #countryFunctionExpression
 | country_by                                     #countryByFunctionExpression
 | amount                                         #amountFunctionExpression
 | currency                                       #currencyFunctionExpression
 | IDENTIFIER                                     #identifierExpression
 | DECIMAL                                        #decimalExpression
 | STRING                                         #stringExpression
 ;