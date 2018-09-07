package com.zhou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages="com.zhou.mapper")
@ComponentScan(basePackages= {"com.zhou", "org.n3r.idworker"})
public class Application { 
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
