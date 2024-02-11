# type-parser
Parse a String and convert it to given type. Lets see how it works.

First we initalize a new TypeCast object. We do this with `TypeCast.newInstance` or with `TypeCast.withSplitter` if we want our own Splitter.
Splitter is used to split the **String** when we want to parse it to a **List**, **Array**, **Map** or anything that might require the given **String** to be split into components.
Here's some examples of parsing.

`typeCast.cast(String[].class, "name1,name2,name3");` parsing into an Array

`typeCast.cast(User.class, "username,true, 100");` parsing into a wrapper Class

This is great, but lets say we want to parse into **List<String**> or any parameterized Class. Then we simply use **TypeToken<Type>** which is a wrapper for any **Type** including **Parameterized** and **GenericArrayType**. Here's how that works.

`typeCast.cast(new TypeToken<List<String>>() {}, "name1,name2,name3");` parsing into a List of strings

`typeCast.cast(new TypeToken<Map<String,Integer>>() {}, "name=100,name2=500,name3=700");` parsing into a Map

We can also nest objects. Lets say we want to parse into a **List** of **User**

`typeCast.cast(new TypeToken<List<User>>() {}, "[name,false,500],[name,true,100],[name,false,0]")` objects are nested inbetween `[]`

Or we can nest an array. Lets say we want to parse into a **List** of **String[]**


`typeCast.cast(new TypeToken<List<String[]>>() {}, "{name1,name2,name3},{name4,name5,name6}");` array is nested inbetween `{}`

We can nest both arrays and objects together.

`typeCast.cast(new TypeToken<Map<User,String[]>>() {}, "[name,true,42]={name1,name2,name3},[name,false,52]={name4,name5,name6}");`
