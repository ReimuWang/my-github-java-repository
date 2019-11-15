package org.reimuwang.filemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileManagementApplication.class, args);
	}

}
