grammar FraudoPayment;

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
 | count_success                                  #countSuccessExpression
 | count_error                                    #countErrorExpression
 | count_chargeback                               #countChargebackExpression
 | count_refund                                   #countRefundExpression
 | sum                                            #sumExpression
 | sum_success                                    #sumSuccessExpression
 | sum_error                                      #sumErrorExpression
 | sum_chargeback                                 #sumChargebackExpression
 | sum_refund                                     #sumRefundExpression
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
