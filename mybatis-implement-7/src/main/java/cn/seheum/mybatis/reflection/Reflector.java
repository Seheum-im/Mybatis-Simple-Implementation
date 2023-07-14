package cn.seheum.mybatis.reflection;

import cn.seheum.mybatis.reflection.invoker.GetFieldInvoker;
import cn.seheum.mybatis.reflection.invoker.Invoker;
import cn.seheum.mybatis.reflection.invoker.MethodInvoker;
import cn.seheum.mybatis.reflection.invoker.SetFieldInvoker;
import cn.seheum.mybatis.reflection.property.PropertyNamer;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author seheum
 * @date 2023/7/10
 */
public class Reflector {

    private static boolean classCacheEnabled = true;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // 线程安全缓存
    private static final Map<Class<?>,Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();

    private Class<?> type;

    // get 属性列表
    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;
    // set 属性列表
    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;
    // set 方法列表
    private Map<String, Invoker> setMethods = new HashMap<>();
    // get 方法列表
    private Map<String, Invoker> getMethods = new HashMap<>();
    // set 类型列表
    private Map<String, Class<?>> setTypes = new HashMap<>();
    // get 类型列表
    private Map<String, Class<?>> getTypes = new HashMap<>();


    private Constructor<?> defaultConstructor;

    private Map<String,String> caseInsensitivePropertyMap = new HashMap<>();

    public Reflector(Class<?> clazz) {
        this.type = clazz;

        //todo 这部分的逻辑实现为什么这样我暂时还没明白，需要完成后debug进行追踪
        // 加入构造函数
        addDefaultConstructor(clazz);
        // 加入getter
        addGetMethods(clazz);
        // 加入Setter
        addSetMethods(clazz);

        // 加入字段
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
        for (String propName : readablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        for (String propName : writeablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
    }


    /*
    得到某个类的反射器，该方法为静态方法，且需要缓存，又要多线程，所以REFLECTOR_MAP 选取了ConcurrentHashMap
     */
    public static Reflector forClass(Class<?> clazz) {
        if (classCacheEnabled) {
            Reflector cached = REFLECTOR_MAP.get(clazz);
            if (cached == null) {
                cached = new Reflector(clazz);
                REFLECTOR_MAP.put(clazz,cached);
            }
            return cached;
        } else {
            return new Reflector(clazz);
        }

    }

    public static boolean isClassCacheEnabled() {
        return classCacheEnabled;
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflector.classCacheEnabled = classCacheEnabled;
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {

                }
            }

            if (field.isAccessible()) {
                if (!setMethods.containsKey(field.getName())) {
                    int modifiers = field.getModifiers();
                    if (!(Modifier.isFinal(modifiers)) && Modifier.isStatic(modifiers)) {
                        addSetField(field);
                    }
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }

    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }

    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            setMethods.put(field.getName(), new SetFieldInvoker(field));
            setTypes.put(field.getName(), field.getType());
        }
    }

    private void addSetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (method.getParameterTypes().length == 1) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingSetters, name, method);
                }
            }
        }
        resolveSetterConflicts(conflictingSetters);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Method firstMethod = setters.get(0);
            if (setters.size() == 1) {
                addSetMethod(propName, firstMethod);
            } else {
                Class<?> expectedType = getTypes.get(propName);
                if (expectedType == null) {
                    throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                            + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                            "specification and can cause unpredicatble results.");
                } else {
                    Iterator<Method> methods = setters.iterator();
                    Method setter = null;
                    while (methods.hasNext()) {
                        Method method = methods.next();
                        if (method.getParameterTypes().length == 1
                                && expectedType.equals(method.getParameterTypes()[0])) {
                            setter = method;
                            break;
                        }
                    }
                    if (setter == null) {
                        throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                                "specification and can cause unpredicatble results.");
                    }
                    addSetMethod(propName, setter);
                }
            }
        }
    }

    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(method));
            setTypes.put(name, method.getParameterTypes()[0]);
        }
    }

    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters,name,method);
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters,name,method);
                }
            }
        }
        resolveGetterConflicts(conflictingGetters);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (String propName : conflictingGetters.keySet()) {
            List<Method> getters = conflictingGetters.get(propName);
            Iterator<Method> iterator = getters.iterator();
            Method firstMethod = iterator.next();
            if (getters.size() == 1) {
                addGetMethod(propName,firstMethod);
            } else {
                Method getter = firstMethod;
                Class<?> getterType = firstMethod.getReturnType();
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    Class<?> methodType = method.getReturnType();
                    if (methodType.equals(getterType)) {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    } else if (methodType.isAssignableFrom(getterType)) {

                    } else if (getterType.isAssignableFrom(methodType)) {
                        getter = method;
                        getterType = methodType;
                    } else {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    }
                }
                addGetMethod(propName,getter);
            }
        }
    }

    private void addGetMethod(String propName, Method firstMethod) {
        if (isValidPropertyName(propName)) {
            getMethods.put(propName,new MethodInvoker(firstMethod));
            getTypes.put(propName,firstMethod.getReturnType());
        }
    }

    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    private void addMethodConflict(Map<String, List<Method>> conflictingGetters, String name, Method method) {
        List<Method> list = conflictingGetters.computeIfAbsent(name, k-> new ArrayList<>());
        list.add(method);
    }

    private Method[] getClassMethods(Class<?> clazz) {
        Map<String, Method> uniqueMethods = new HashMap<String, Method>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods,currentClass.getDeclaredMethods());

            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods,anInterface.getMethods());
            }

            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                //获得签名
                String signature = getSignature(currentMethod);

                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {

                        }
                    }
                    uniqueMethods.put(signature,currentMethod);
                }
            }

        }
    }

    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i == 0) {
                sb.append(':');
            }else {
                sb.append(',');
            }
            sb.append(parameterTypes[i].getName());
        }
        return sb.toString();
    }

    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception ignore) {

                    }
                }
                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }
    }

    private static boolean canAccessPrivateMethods(){
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    public Class<?> getGetterType(String name) {
        Class<?> clazz = getTypes.get(name);
        if (clazz == null) {
            throw new RuntimeException("There is no getter for property named '" + name + "' in '" + type + "'");
        }
        return clazz;
    }

    public Class<?> getSetterType(String name) {
        Class<?> clazz = setTypes.get(name);
        if (clazz == null) {
            throw new RuntimeException("There is no setter for property named '" + name + "' in '" + type + "'");
        }
        return clazz;
    }

    public String findPropertyName(String name) {
        return caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    public String[] getGetablePropertyNames() {
        return readablePropertyNames;
    }

    public String[] getSetablePropertyNames() {
        return writeablePropertyNames;
    }


    public Invoker getGetInvoker(String name) {
        Invoker method = getMethods.get(name);
        if (method == null) {
            throw new RuntimeException("There is no getter for property named '" + name + "' in '" + type + "'");
        }
        return method;
    }
}
