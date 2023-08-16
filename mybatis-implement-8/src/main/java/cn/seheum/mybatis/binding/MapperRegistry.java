package cn.seheum.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class MapperRegistry {


    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 将已添加的mapper代理加入
     */
    private final Map<Class<?>,MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
       final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
       if(mapperProxyFactory == null) {
           throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
       }
       try{
           return mapperProxyFactory.newInstance(sqlSession);
       }catch (Exception e) {
           throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
       }
    }

    public <T> void addMapper(Class<T> type) {
        //mapper 必须是接口才会注册
        if(type.isInterface()) {
            if(hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            knownMappers.put(type,new MapperProxyFactory<>(type));
        }
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    public  <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }


}
