package cn.seheum.mybatis.reflection.factory;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

/**
 * @author seheum
 * @date 2023/8/9 9:18
 **/
public class DefaultObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -64521541636546853L;

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public <T> T create(Class<T> type) {
        return null;
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return null;
    }

    @Override
    public <T> boolean isCollection(Class<?> type) {
        return false;
    }
}
