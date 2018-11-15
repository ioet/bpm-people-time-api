package com.ioet.bpm.peopletime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BpmPeopleTimeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmPeopleTimeApiApplication.class, args);
	}
}
