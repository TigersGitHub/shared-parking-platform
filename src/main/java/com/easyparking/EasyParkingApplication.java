package com.easyparking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统启动类
 */
// 扫描com.easyparking.dao路径下的类
@MapperScan("com.easyparking.dao")
// SpringBoot工程的专用注解
@SpringBootApplication
public class EasyParkingApplication {

	/**
	 * main方法，执行SpringBoot程序的启动
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(EasyParkingApplication.class, args);
	}

}
