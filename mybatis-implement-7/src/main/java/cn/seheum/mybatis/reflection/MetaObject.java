package cn.seheum.mybatis.reflection;

import cn.seheum.mybatis.reflection.factory.ObjectFactory;
import cn.seheum.mybatis.reflection.wrapper.ObjectWrapper;
import cn.seheum.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * @author seheum
 * @date 2023/7/15 5:00
 **/
public class MetaObject {

    //原始对象
    private Object originalObject;

    //对象包装器
    private ObjectWrapper objectWrapper;

    //对象工厂
    private ObjectFactory objectFactory;

    //对象包装工厂
    private ObjectWrapperFactory objectWrapperFactory;
}
