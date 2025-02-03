package com.project.income;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class IncomeMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncomeMicroserviceApplication.class, args);
	}

}
