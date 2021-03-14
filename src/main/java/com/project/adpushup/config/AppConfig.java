package com.project.adpushup.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static com.project.adpushup.constant.Constants.URL;
import static com.project.adpushup.constant.Constants.DRIVER;
import static com.project.adpushup.constant.Constants.PASSWORD;
import static com.project.adpushup.constant.Constants.USER;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.project.adpushup")
public class AppConfig {

    public DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(URL);
        driverManagerDataSource.setUsername(USER);
        driverManagerDataSource.setPassword(PASSWORD);
        driverManagerDataSource.setDriverClassName(DRIVER);
        return driverManagerDataSource;
    }
}

