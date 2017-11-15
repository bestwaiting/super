package com.bestwaiting.multidao.datasource;

import org.springframework.util.Assert;

/**
 * Created by bestwaiting on 17/6/20.
 */
public class DataSourceContext {
    // 线程局部变量（多线程并发设计，为了线程安全）
    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    // 设置数据源类型
    public static void setDataSourceType(String dataSourceType) {
        Assert.notNull(dataSourceType, "DataSourceType cannot be null");
        contextHolder.set(dataSourceType);
    }

    // 获取数据源类型
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    // 清除数据源类型
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
