package cn.seheum.mybatis.reflection.invoker;

/**
 * @author seheum
 * @date 2023/7/10
 * 对解析的类中属性对应的setter/getter 或者普通函数进行解析的统一接口，根据setter/getter 或者普通的method来采取不同策略实现
 */
public interface Invoker {
    Object invoke(Object target,Object[] args) throws Exception;

    Class<?> getType();
}
