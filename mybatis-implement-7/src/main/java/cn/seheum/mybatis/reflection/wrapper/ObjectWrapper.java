package cn.seheum.mybatis.reflection.wrapper;

import cn.seheum.mybatis.reflection.MetaObject;
import cn.seheum.mybatis.reflection.factory.ObjectFactory;
import cn.seheum.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * @author seheum
 * @date 2023/7/14 12:52
 **/
public interface ObjectWrapper {

    //get
    Object get(PropertyTokenizer prop);

    //set
    void set(PropertyTokenizer prop, Object value);

    //查找属性
    String findProperty(String name, boolean useCamelCaseMapping);

    //获取getter的名字列表
    String[] getGetterNames();

    //取得setter的名字列表
    String[] getSetterNames();

    //取得setter的类型
    Class<?> getSetterType(String name);

    //取得getter的类型
    Class<?> getGetterType(String name);


    //判断是否有指定的setter
    boolean hasSetter(String name);

    //判断是否有指定的getter
    boolean hasGetter(String name);

    //实例化属性
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

    //判断是否为集合
    boolean isCollection();

    //添加属性
    void add(Object element);

    //添加属性
    <E> void addAll(List<E> element);


}
