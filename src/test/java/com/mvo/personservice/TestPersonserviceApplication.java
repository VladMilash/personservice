package com.mvo.personservice;

import org.springframework.boot.SpringApplication;

public class TestPersonserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PersonServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
