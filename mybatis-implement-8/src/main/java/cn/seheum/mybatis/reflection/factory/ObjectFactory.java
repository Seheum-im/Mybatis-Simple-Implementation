package cn.seheum.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * @author seheum
 * @date 2023/7/15 5:03
 **/
public interface ObjectFactory {

    //设置属性
    void setProperties(Properties properties);

    //创建对象
    <T> T create(Class<T> type);

    //使用指定规定构造函数和构造函数参数创建对象
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    //是否为集合
    <T> boolean isCollection(Class<?> type);

}
