## Mapper代理以及代理工厂的落地实现

> ORM框架是什么？
- JDBC是Java程序与关系型数据库的一个驱动的接口，解耦了应用程序和数据库连接，但是程序中的OOM思想与关系型数据库不能够对应匹配得上，
这样会导致OOM的编程思想被遏制,Java的一切皆为对象的编程思想无法落实到操作数据库上
- ORM(Object/Relational Mapping) 对象-关系映射，将对象和关系型数据库进行映射，相当于是把关系型数据库通过一层包装成对象型数据库，底层还封装了JDBC与数据库进行交互，
提供了许多强大的附加功能

1.目标：为Dao层使用代理类，封装mapper操作

2.设计

3.功能模块：  
1）MapperProxy
- 代理类包装对数据库的操作
- 实现了InvocationHandler，接收传输来的sqlSession和mapperInterface，执行Dao层接口函数的调用

2）MapperProxyFactory
- 对MapperProxy提供工厂实例化MapperProxyFactory#newInstance,为每一个IDAO接口生成代理类。

3）IUserDao  
- Dao层接口，定义接口方法，需要被代理


4.测试执行流程  

1)创建MapperProxyFactory对象，传入IUserDao.Class 参数，表明工厂创建IUserDao的代理类.   

2)封装sqlSession，模拟操作数据库操作  

3)工厂实例化代理对象，传入sqlSession  

4)代理对象执行Dao层接口对应的函数，本质是代理对象中调用了invoke()



