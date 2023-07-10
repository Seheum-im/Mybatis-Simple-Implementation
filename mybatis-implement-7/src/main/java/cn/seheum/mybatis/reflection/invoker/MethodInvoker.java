package cn.seheum.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * @author seheum
 * @date 2023/7/10
 */
public class MethodInvoker implements Invoker {

    private Class<?> type;

    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        // 若只有一个参数，则返回参数类型，否则返回return类型
        if(method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        }else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return method.invoke(target,args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
