package cn.seheum.mybatis.reflection.factory;


import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author seheum
 * @date 2023/8/7 9:18
 **/
public class DefaultObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -64521541636546853L;

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public <T> T create(Class<T> type) {
        return create(type, null, null );
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        //解析接口
        Class<?> classToCreate = resolveInterface(type);

        //类实例化
        return (T) instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
    }

    private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {

            Constructor<T> constructor;
            //如果没有传入构造函数，则调用空构造函数
            if (constructorArgTypes == null || constructorArgs == null) {
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }

            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));

        } catch (Exception e) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null) {
                for (Class<?> argType : constructorArgTypes) {
                    argTypes.append(argType.getSimpleName());
                    argTypes.append(",");
                }
            }

            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null) {
                for (java.lang.Object argValue : constructorArgs) {
                    argValues.append(argValue);
                    argValues.append(",");
                }
            }
            throw new RuntimeException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + e, e);
        }

    }

    protected Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            // List|Collection|Iterable-->ArrayList
            classToCreate = ArrayList.class;
        } else if (type == Map.class) {
            // Map->HashMap
            classToCreate = HashMap.class;
        } else if (type == SortedSet.class) {
            // SortedSet->TreeSet
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            // Set->HashSet
            classToCreate = HashSet.class;
        } else {
            // 除此以外，就用原来的类型
            classToCreate = type;
        }
        return classToCreate;
    }

    @Override
    public <T> boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }
}
