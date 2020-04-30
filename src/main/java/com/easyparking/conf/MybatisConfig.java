package com.easyparking.conf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * MyBatis配置类
 * 配置了数据库相关配置项
 * （因为用了Druid，这里大部分配置都是从Druid配置类里读过来的，没啥区别）
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfig {
	private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

	@Autowired
	private DruidDataSourceConfig druidDataSourceConfig;

	@Bean
	public DataSource dataSource() {
		if(logger.isDebugEnabled()){
			logger.debug("druidDataSourceConfig" + druidDataSourceConfig);
		}
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(druidDataSourceConfig.getDriverClassName());
		ds.setUsername(druidDataSourceConfig.getUsername());
		ds.setPassword(druidDataSourceConfig.getPassword());
		ds.setUrl(druidDataSourceConfig.getUrl());
		ds.setMaxActive(druidDataSourceConfig.getMaxActive());
		ds.setValidationQuery(druidDataSourceConfig.getValidationQuery());
		ds.setTestOnBorrow(druidDataSourceConfig.isTestOnBorrow());
		ds.setTestOnReturn(druidDataSourceConfig.isTestOnReturn());
		ds.setTestWhileIdle(druidDataSourceConfig.isTestWhileIdle());
		ds.setTimeBetweenEvictionRunsMillis(druidDataSourceConfig.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(druidDataSourceConfig.getMinEictableIdleTimeMillis());
		ds.setPoolPreparedStatements(druidDataSourceConfig.isPoolPreparedStatements());
		ds.setMaxOpenPreparedStatements(druidDataSourceConfig.getMaxOpenPreparedStatements());
		try {
			ds.setFilters(druidDataSourceConfig.getFilters());
		} catch (SQLException e) {
			//e.printStackTrace();
			logger.error(e.getSQLState());
		}
		return ds;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("--> sqlSessionFactory");
		}
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		sqlSessionFactory.setFailFast(true);
		//sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		Properties sqlSessionFactoryProperties = new Properties();
		sqlSessionFactoryProperties.setProperty("cacheEnabled", "true");
		sqlSessionFactoryProperties.setProperty("lazyLoadingEnabled", "true");
		sqlSessionFactoryProperties.setProperty("aggressiveLazyLoading", "true");
		sqlSessionFactoryProperties.setProperty("useGeneratedKeys", "false");
		sqlSessionFactoryProperties.setProperty("multipleResultSetsEnabled", "true");
		//sqlSessionFactoryProperties.setProperty("autoMappingBehavior", "PARTIAL");
		sqlSessionFactoryProperties.setProperty("mapUnderscoreToCamelCase", "true");
		//sqlSessionFactoryProperties.setProperty("useColumnLabel", "true");
		//SIMPLE REUSE BATCH
		sqlSessionFactoryProperties.setProperty("defaultExecutorType", "SIMPLE");
		sqlSessionFactoryProperties.setProperty("defaultStatementTimeout", "25");
		sqlSessionFactoryProperties.setProperty("jdbcTypeForNull", "NULL");
		sqlSessionFactoryProperties.setProperty("logImpl", "SLF4J");
		sqlSessionFactory.setConfigurationProperties(sqlSessionFactoryProperties);
      
		sqlSessionFactory.setMapperLocations(getResource("mapper", "**/*.xml"));
		return sqlSessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	public Resource[] getResource(String basePackage, String pattern) throws IOException {
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(basePackage)) + "/" + pattern;
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
		return resources;
	}
	
}
