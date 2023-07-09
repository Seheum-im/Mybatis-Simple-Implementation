## 解析XML文件

1.目标
- 上节主要是提供一个MapperRegistry，一个MapperProxyFactory的注册器，且每一IDao注册时都能够对应着一个MapperProxyFactory，
 同时提供了一个包的路径扫描和读取注册，以及对SqlSession进行规范化处理。
 - 而本节主要是实现对Mapper XML文件的解析
 
 