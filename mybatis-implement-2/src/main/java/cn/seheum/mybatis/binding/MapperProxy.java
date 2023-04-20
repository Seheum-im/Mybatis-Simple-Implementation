package cn.seheum.mybatis.binding;

import cn.seheum.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author seheum
 * @date 2023/4/18
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 55423641653213L;

    private SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
        }else {
            return sqlSession.selectOne(method.getName(),args);
        }
    }
}
