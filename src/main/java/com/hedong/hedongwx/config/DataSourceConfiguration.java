package com.hedong.hedongwx.config;

/**
 * 配置datasource到ioc容器里面
 * 
 * @author adminHEDONG
 *
 */
//@Configuration
// 配置mybatis mapper的扫描路径
//@MapperScan("com.hedong.hedongwx.dao")
public class DataSourceConfiguration {
	/*@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;

	*//**
	 * 生成与spring-dao.xml对应的bean dataSource
	 * 
	 * @return
	 * @throws PropertyVetoException
	 *//*
	@Bean(name = "dataSource")
	public ComboPooledDataSource createDataSource() throws PropertyVetoException {
		// 生成datasource实例
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		// 跟配置文件一样设置以下信息
		// 驱动
		dataSource.setDriverClass(jdbcDriver);
		// 数据库连接URL
		dataSource.setJdbcUrl(jdbcUrl);
		// 设置用户名
		dataSource.setUser(jdbcUsername);
		// 设置用户密码
		dataSource.setPassword(jdbcPassword);
		// 配置c3p0连接池的私有属性
		// 连接池最大线程数
		dataSource.setMaxPoolSize(100);
		// 连接池最小线程数
		dataSource.setMinPoolSize(10);
		// 关闭连接后不自动commit
		dataSource.setAutoCommitOnClose(false);
		// 连接超时时间
		dataSource.setCheckoutTimeout(20000);
		// 连接失败重试次数
		dataSource.setAcquireRetryAttempts(2);
		// 重新尝试的时间间隔
		dataSource.setAcquireRetryDelay(1000);
		// 连接测试语句
		dataSource.setPreferredTestQuery("select 1 from hd_server_receiveinfo");
		// 测连接有效的时间间隔
		dataSource.setIdleConnectionTestPeriod(18000);
		// 如果设为true那么在取得连接的同时将校验连接的有效性。Default: false
		dataSource.setTestConnectionOnCheckin(true);
		//最大空闲时间，多少秒内未使用则连接被丢弃。若为0则永不丢弃。默认值: 0
		dataSource.setMaxIdleTime(60);
		//每隔多少秒检查所有连接池中的空闲连接。Default: 0
		dataSource.setIdleConnectionTestPeriod(60);
		return dataSource;
	}*/

}
