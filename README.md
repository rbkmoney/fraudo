# fraudo

![alt text](logo.jpg)

#### Синтаксис

![alt text](syntax.png)

##### OPERATIONS:
~~~~
*   count("group_field", time_in_minut)
*   countSuccess("group_field", time_in_minut)
*   countError("group_field", time_in_minut, "error_code")
*   sum("group_field", time_in_minut)
*   sumSuccess("group_field", time_in_minut)
*   sumError(("group_field", time_in_minut, "error_code")
*   unique(("group_field", "by_field")
*   in(("field", "first", "second", ...)
*   inWhiteList("field")
*   inBlackList("field")
*   like("field", "regexp_in_java_style")
*   equalsCountry()
~~~~
##### RESULTS:
~~~~
*   accept 
*   3ds
*   decline
*   notify
~~~~