# Fraudo DSL

Language for describing antifraud patterns

Provides the ability to describe the required set of rules for characteristics
and triggers for attempts at fraudulent actions

#### Syntax

![alt text](syntax.png)

##### OPERATIONS:
~~~~
*   count("group_field", time_in_minutes|[from_offset, to_offset], ["group_by_additional_fields"])
*   countSuccess("group_field", time_in_minutes|[from_offset, to_offset], ["group_by_additional_fields"])
*   countError("group_field", time_in_minutes|[from_offset, to_offset], "error_code", ["group_by_additional_fields"])
*   sum("group_field", time_in_minutes|[from_offset, to_offset], ["group_by_additional_fields"])
*   sumSuccess("group_field", time_in_minutes|[from_offset, to_offset], ["group_by_additional_fields"])
*   sumError(("group_field", time_in_minutes|[from_offset, to_offset], "error_code", ["group_by_additional_fields"])
*   unique(("group_field", "by_field",time_in_minutes|[from_offset, to_offset], ["group_by_additional_fields"])
*   in(("field", "first", "second", ...)
*   inWhiteList("field")
*   inBlackList("field")
*   inList("test", "email")
*   inGreyList("email")
*   like("field", "regexp_in_java_style"[1])
*   amount()
*   country() - this function can return result "unknown", you must remember it!
~~~~

### group_field:
  *  email,
  *  ip,
  *  fingerprint,
  *  bin,
  *  shop_ip,
  *  party_id,
  *  card_token
    
1. [regexp_in_java_style](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
### RESULTS:
~~~~
*   accept 
*   3ds
*   decline
*   notify
*   normal
~~~~
### EXAMPLES:
###### Simple:
~~~~
rule: 3 > 2 AND 1 = 1
-> accept;
~~~~
###### Black list check:
~~~~
rule: inBlackList("email")
-> notify;
~~~~
###### Counts check:
~~~~
rule: (count("ip", 1444) >= 10 OR countSuccess("email", 1444) > 5)
        AND countError("fingerprint", 1444, "error_code") > 5
-> notify;
~~~~
###### Unique count emails for ip:
~~~~
rule: unique("email", "ip") < 4
-> decline;
~~~~
###### Check country by ip:
~~~~
rule: country() = "RU"
-> notify;
~~~~
###### Check current amount:
~~~~
rule: amount() < 100
-> accept;
~~~~
###### Combined check:
~~~~
rule:
inWhiteList("email", "fingerprint", "card", "bin", "ip") -> accept; # принимаем платеж, если хотя бы один из указанных параметров находится в вайтлисте

rule:
inBlackList("email", "fingerprint", "card", "bin", "ip") -> decline; # отклоняем платеж, если хотя бы один из указанных параметров находится в блэклисте

rule:
in(countryBy("bin"), "AS", "SD", "TR", "WE", "SD", "CD", "KL", "EW", "VF", "XZ", "CD") -> decline; # эти страны блочим всегда

rule:
amount() > 1000 AND in(countryBy("bin"), "DS", "LA", "AS") -> decline; # лимит суммы платежа 10 баксов для 

rule:
amount() > 1000 AND in(countryBy("bin"), "VC", "WE") -> decline;# лимит суммы платежа 10 баксов для некоторых стран

rule:
amount() > 10000 -> decline;# лимит суммы платежа 100 баксов для всех остальных

rule:
count("card", 1440) > 10 AND in (countryBy("bin"), "TR", "WE", "SD", "CD", "KL", "EW") -> decline;# этим странам 10 попыток с одной карты в сутки

rule:
count("card", 1440) > 5 -> decline;# остальным странам 5 попыток с одной карты в сутки

rule:
unique("card", "email", 1440) > 3 -> decline; # лимит 3 уникальных карты на емэйл за сутки

rule:
unique("card", "fingerprint", 1440) > 3 -> decline; # лимит 3 уникальных карты на девайс за сутки
~~~~
