package net.fuyuko.projectmanagement;

import org.springframework.boot.SpringApplication;

public class TestProjectmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProjectmanagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
