package cn.seheum.mybatis.reflection;


/**
 * @author seheum
 * @date 2023/7/12
 */
public class MetaClass {

    private Reflector reflector;

    private MetaClass(Class<?> type) {
        this.reflector = Reflector.forClass(type);
    }

    public static MetaClass forClass(Class<?> type) {
        return new MetaClass(type);
    }

    public static boolean isClassCacheEnabled() {
        return Reflector.isClassCacheEnabled();
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflector.setClassCacheEnabled(classCacheEnabled);
    }

    public MetaClass metaClassForProperty(String name) {
        Class<?> propType = reflector.getGetterType(name);
        return MetaClass.forClass(propType);
    }

    public String findProperty(String name) {
        StringBuilder prop = buildProperty(name,new StringBuilder());
    }

    private StringBuilder buildProperty(String name, StringBuilder stringBuilder) {
    }
}
