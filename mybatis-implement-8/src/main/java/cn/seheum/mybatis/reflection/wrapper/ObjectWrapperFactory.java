package cn.seheum.mybatis.reflection.wrapper;

import cn.seheum.mybatis.reflection.MetaObject;

/**
 * @author seheum
 * @date 2023/7/15 5:17
 **/
public interface ObjectWrapperFactory {

    /**
     * 判断有没有包装器
     */
    boolean hasWrapperFor(Object object);

    /**
     * 得到包装器
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
