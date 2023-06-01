package cn.seheum.mybatis.session;

import cn.seheum.mybatis.binding.MapperRegistry;
import cn.seheum.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.seheum.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.seheum.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.seheum.mybatis.executor.Executor;
import cn.seheum.mybatis.executor.SimpleExecutor;
import cn.seheum.mybatis.executor.resultset.DefaultResultSetHandler;
import cn.seheum.mybatis.executor.resultset.ResultSetHandler;
import cn.seheum.mybatis.executor.statement.PreparedStatementHandler;
import cn.seheum.mybatis.executor.statement.StatementHandler;
import cn.seheum.mybatis.mapping.BoundSql;
import cn.seheum.mybatis.mapping.Environment;
import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.transaction.Transaction;
import cn.seheum.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.seheum.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class Configuration {

    //配置文件环境
    protected Environment environment;

    //类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();


    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    /**
     * 创建结果集处理器
     * @param executor
     * @param mappedStatement
     * @param boundSql
     * @return
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor,ms,parameter,resultHandler,boundSql);
    }

    public Executor newExecutor(Transaction tx) {
        return new SimpleExecutor(this,tx);
    }
}
