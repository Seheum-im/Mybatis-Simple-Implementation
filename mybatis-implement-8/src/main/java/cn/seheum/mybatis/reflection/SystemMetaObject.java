package cn.seheum.mybatis.reflection;

import cn.seheum.mybatis.reflection.factory.DefaultObjectFactory;
import cn.seheum.mybatis.reflection.factory.ObjectFactory;
import cn.seheum.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.seheum.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * @author seheum
 * @date 2023/8/9 9:14
 **/
public class SystemMetaObject {

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);


    private SystemMetaObject() {
    }

    private static class NullObject {

    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
}
