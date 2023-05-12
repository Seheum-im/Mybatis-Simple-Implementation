package cn.seheum.mybatis.binding;

import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.mapping.SqlCommandType;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author seheum
 * @date 2023/4/21
 */
public class MapperMethod {

    private final SqlCommand command;


    public MapperMethod(Class<?> mapperInterface,Method method,Configuration configuration) {
        this.command = new SqlCommand(configuration,mapperInterface,method);
    }

    public Object execute(SqlSession sqlSession,Object[] args) {
        //TODO 继续完成根据类型判断是哪种类型的sql
        Object result = null;
        switch (command.getType()) {
            case DELETE:
                break;
            case INSERT:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(),args);
                break;
            case UPDATE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;

    }


    public static class SqlCommand {
        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(statementName);
            name = mappedStatement.getId();
            type = mappedStatement.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }
}
