package cn.seheum.mybatis.reflection.property;

import java.util.Iterator;

/**
 * @author seheum
 * @date 2023/7/12
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {

    // 例子：班级[0].学生.成绩

    private String name;    // 班级

    private String indexedName;// 班级[0]


    private String index; // 0

    private String children;// 学生.成绩


    public PropertyTokenizer(String fullName) {
        //找到输入例子中的第一个 "."，因为数据库中的数据以字符串的形式带 "."的形式输入
        int pointIdx = fullName.indexOf('.');
        if (pointIdx > -1) {
            name = fullName.substring(0,pointIdx);
            children = fullName.substring(pointIdx + 1);
        } else {
            name = fullName;
            children = null;
        }
        indexedName = name;

        //查找中括号，解析中括号内的数字
        pointIdx = name.indexOf('[');
        if (pointIdx > -1) {
            index = name.substring(pointIdx + 1, name.length() - 1);
            name = name .substring(0,pointIdx);
        }
    }
    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }
}
