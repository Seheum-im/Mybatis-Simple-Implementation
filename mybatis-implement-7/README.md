## 采用反射解析数据源

1.目标
- 上一节实现了……
- 数据源实例化时，对属性值信息获取采用了硬编码的方式，对后续配置新的字段没有得到很好的扩展，代码如下:
``UnpooledDataSource dataSource = new UnpooledDataSource(); ``
         `` dataSource.setDriver(properties.getProperty("driver"));``
          ``dataSource.setUrl(properties.getProperty("url"));``
          ``dataSource.setUsername(properties.getProperty("username"));``
         `` dataSource.setPassword(properties.getProperty("password"));``
- 本节采用了反射对数据源中的属性值解析，解耦
       
2.设计
- 在为数据源设置属性值前，对数据源类中的属性值进行解析，首先通过调用SystemMetaObject.forObject()，创建一个MetaObject,
然后判断object是哪种类型的数据，进行实例化ObjectWrapper（此处的Object为Bean对象，实例化BeanWrapper）
实例化的BeanWrapper调用MetaClass.forClass(Class<?> type),将Object对象的类型传输过去，实例化MetaClass,
实例化MetaClass时调用了Reflector.forClass(),最终在Reflector中解析了Object内的属性值以及方法   

3.新增模块

1）Invoker
- 对解析的类中属性对应的setter/getter 或者普通函数进行解析的统一接口，
根据setter/getter 或者普通的method来采取不同策略实现 

1.1)MethodInvoker

1.2)SetFieldInvoker

1.3)GetFieldInvoker

2）Reflector
- 对传输来的类进行解析，解析出类中的属性类型和方法，是整个反射过程中实际上执行解析操作的类  

3）MetaClass 
- 在Reflector上的一层封装，对外提供Reflector中的属性获取方法以及执行实例化Reflector

4）ObjectWrapper
- 对象包装器的统一接口，定义了类的属性值、属性类型以及方法名称

4.1）BaseWrapper

4.2）BeanWrapper

4.3）MapWrapper

4.4）CollectionWrapper
         
