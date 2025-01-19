package com.example.purchase.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import com.example.purchase.management.config.ReportProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.purchase.management.config.EmailProperties;

@EntityScan("com.example.purchase.management.entity")
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(EmailProperties.class)


public class PurchaseManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseManagementApplication.class, args);
	}

}
