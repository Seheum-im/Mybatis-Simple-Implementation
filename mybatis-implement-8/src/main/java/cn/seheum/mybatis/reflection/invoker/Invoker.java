package cn.seheum.mybatis.reflection.invoker;

/**
 * @author seheum
 * @date 2023/7/10
 */
public interface Invoker {
    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();
}
