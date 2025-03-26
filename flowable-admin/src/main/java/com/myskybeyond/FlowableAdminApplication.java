package com.myskybeyond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mySkyBeyond
 * 启动类
 */
@SpringBootApplication(scanBasePackages = {"org.dromara", "com.myskybeyond"})
public class FlowableAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableAdminApplication.class, args);
	}

}
