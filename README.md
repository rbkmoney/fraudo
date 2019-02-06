# fraudo

![alt text](logo.jpg)

#### Syntax

![alt text](syntax.png)

##### OPERATIONS:
~~~~
*   count("group_field", time_in_minutes)
*   countSuccess("group_field", time_in_minutes)
*   countError("group_field", time_in_minutes, "error_code")
*   sum("group_field", time_in_minutes)
*   sumSuccess("group_field", time_in_minutes)
*   sumError(("group_field", time_in_minutes, "error_code")
*   unique(("group_field", "by_field")
*   in(("field", "first", "second", ...)
*   inWhiteList("field")
*   inBlackList("field")
*   like("field", "regexp_in_java_style"[1])
*   amount()
*   country() - this function can return result "unknown", you must remember it!
~~~~

##### group_field:
  *  email,
  *  ip,
  *  fingerprint,
  *  bin,
  *  shop_ip,
  *  party_id,
  *  card_token
    
1. [regexp_in_java_style](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
##### RESULTS:
~~~~
*   accept 
*   3ds
*   decline
*   notify
*   normal
~~~~
##### EXAMPLES:
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
rule: 3 > 2 AND 1 > 1
-> decline;

rule: count("email", 10) <= 10 AND count("ip", 1444) = 10
-> 3ds;
~~~~