package com.bestwaiting.dao;

import com.bestwaiting.dao.config.DaoConfig;
import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by bestwaiting on 17/6/2.
 */
@Configuration
@ComponentScan(basePackages = "com.bestwaiting.dao")
@PropertySource(value = {"classpath:conf/mybatis-dao.properties"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DaoContext {

    @Autowired
    DaoConfig daoConfig;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
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
     * spring和MyBatis完美整合，不需要mybatis的配置映射文件
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);

        //添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});

        // 设置查找器
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * DAO接口所在包名，Spring会自动查找其下的类
     *
     * @return
     */
    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.bestwaiting.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate() throws Exception {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager());
        return transactionTemplate;
    }
}
