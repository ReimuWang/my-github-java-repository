package org.reimuwang.testspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class TestSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestSpringbootApplication.class, args);
	}

}
