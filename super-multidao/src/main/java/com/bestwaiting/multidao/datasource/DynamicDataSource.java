package com.bestwaiting.multidao.datasource;

import com.bestwaiting.multidao.config.DaoConfig;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;


/**
 * Created by bestwaiting on 17/6/19.
 */
@Slf4j
@Configuration
public class DynamicDataSource extends AbstractRoutingDataSource  {

    @Autowired
    DaoConfig daoConfig;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(daoConfig.getDriverClass());
        dataSource.setJdbcUrl(daoConfig.getJdbcUrl());
        dataSource.setUsername(daoConfig.getJdbcUser());
        dataSource.setPassword(daoConfig.getJdbcPassword());
        dataSource.setIdleTimeout(60000);
        dataSource.setMinimumIdle(0);
        dataSource.setMaxLifetime(700000);
        dataSource.setMaximumPoolSize(30);
        dataSource.setConnectionTestQuery("SELECT 1 FROM DUAL");
        dataSource.setConnectionTimeout(10000);

        Properties properties = new Properties();
        properties.put("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        properties.put("cachePrepStmts", true);
        properties.put("prepStmtCacheSize", 250);
        properties.put("prepStmtCacheSqlLimit", 2048);
        dataSource.setDataSourceProperties(properties);

        return dataSource;
    }


    /**
     * 根据相应参数创建对应的数据源
     *
     * @return
     */
    @Bean(destroyMethod = "close")
    public DataSource testDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:8709/mutitest?characterEncoding=utf-8&characterSetResults=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setIdleTimeout(60000);
        dataSource.setMinimumIdle(0);
        dataSource.setMaxLifetime(700000);
        dataSource.setMaximumPoolSize(30);
        dataSource.setConnectionTestQuery("SELECT 1 FROM DUAL");
        dataSource.setConnectionTimeout(10000);

        Properties properties = new Properties();
        properties.put("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        properties.put("cachePrepStmts", true);
        properties.put("prepStmtCacheSize", 250);
        properties.put("prepStmtCacheSqlLimit", 2048);
        dataSource.setDataSourceProperties(properties);

        return dataSource;
    }


    /**
     * 设置默认的数据源
     *
     * @param defaultTargetDataSource
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(dataSource());
    }

    @Override
    public void afterPropertiesSet() {
        this.setTargetDataSources(null);
        this.setDefaultTargetDataSource(null);
        super.afterPropertiesSet();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {

        Map<Object, Object> target = Maps.newHashMap();
        target.put("test", testDataSource());
        super.setTargetDataSources(target);

    }

    /**
     * 设置系统当前使用的数据源
     * （1）首先检测key是否存在于当前数据源集合中，若不存在，更新数据源；
     * （2）若不存在，返回默认数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {

        return DataSourceContext.getDataSourceType();
    }
}
