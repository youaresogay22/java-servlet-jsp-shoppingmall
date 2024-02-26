package com.nhnacademy.shoppingmall.common.config;

import com.nhnacademy.shoppingmall.Base;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = Base.class,
        excludeFilters = @ComponentScan.Filter(Controller.class))
public class RootConfig {
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://133.186.223.228/nhn_academy_29");
        dataSource.setUsername("nhn_academy_29");
        dataSource.setPassword("O8jJitFThm]dssG4");

        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxIdle(10);

        dataSource.setMaxWaitMillis(1000);

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        return dataSource;
    }

}
