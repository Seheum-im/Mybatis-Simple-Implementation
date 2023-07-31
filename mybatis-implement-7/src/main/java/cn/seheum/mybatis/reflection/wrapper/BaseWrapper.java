package cn.seheum.mybatis.reflection.wrapper;

import cn.seheum.mybatis.reflection.MetaObject;
import cn.seheum.mybatis.reflection.property.PropertyTokenizer;

/**
 * @author seheum
 * @date 2023/7/15 5:19
 **/
public abstract class BaseWrapper implements ObjectWrapper {

    protected static final Object[] NO_ARGUMENTS = new Object[0];
    protected MetaObject metaObject;

    protected BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    /**
     * 集合解析
     * @param prop
     * @param object
     * @return
     */
    protected Object resolveCollection(PropertyTokenizer prop, Object object) {

    }
}
