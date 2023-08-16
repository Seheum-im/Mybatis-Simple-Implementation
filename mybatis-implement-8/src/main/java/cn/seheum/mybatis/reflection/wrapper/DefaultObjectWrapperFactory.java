package cn.seheum.mybatis.reflection.wrapper;

import cn.seheum.mybatis.reflection.MetaObject;

/**
 * @author seheum
 * @date 2023/8/9 9:21
 **/
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
