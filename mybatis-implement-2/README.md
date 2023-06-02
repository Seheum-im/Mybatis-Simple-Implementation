# mapper注册器的实现

1.目标
- 上一节主要是通过MapperProxyFactory创建出MapperProxy，对Dao层接口的函数进行代理实现，执行类似操作数据库的操作
- 但是上一节需要手动传Dao层的IDao作为代理工厂MapperProxyFactory的泛型，才能够创建出对应的代理类MapperProxy
- 本节主要是为了提供一个MapperRegistry，一个MapperProxyFactory的注册器，且每一IDao注册时都能够对应着一个MapperProxyFactory，
同时提供了一个包的路径扫描和读取注册，以及对SqlSession进行规范化处理

2.系统设计：包装一个扫描Dao层包完成MapperProxyFactory的注册，以及完善SqlSession,由SqlSession定义数据库执行接口和获取Mapper，
并将它传给代理类使用,且对SqlSession提供工厂模式，使用SqlSessionFactory生成SqlSession

3.项目新增 
 
1）MapperRegistry  
- addMappers()用于扫描包路径以及注册MapperProxyFactory，创建对应IDao的MapperProxy
- 为每一个IDao接口注册MapperProxyFactory，放入knownMappers的哈希表中
- 调用getMapper()时传入IDao接口的类型和SqlSession，从knownMappers中获取对应的MapperProxyFactory，
然后生成对应的代理类对象

2）SqlSession
- 在本节中SqlSession主要是模仿操作数据库，SqlSession定义了函数，由实现类DefaultSession来实现，
通过getMapper()获取对应的代理类对象。代理对象执行IDao接口中的函数时，调用invoke()去执行SqlSession中定义的模拟操作数据库的函数

3）SqlSessionFactory
- 主要作为生成SqlSession的工厂，传入MapperRegistry参数


4.测试执行流程  

1）创建MapperRegistry对象，添加需要扫描的包名完整路径  
2）实例化SqlSessionFactory，传入MapperRegistry参数  
3）通过SqlSessionFactory创建SqlSession  
4）获取对应的IDao的代理对象  
5）代理对象执行IDao中定义的函数


5.总结
- Mybatis目前主要使用了简单的工厂模式，对工厂生成的对象进行了封装，提供对外访问的接口，让子类决定实例化对象的类型


