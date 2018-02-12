package net.zypro.feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.zypro.feed")
@EnableAutoConfiguration
public class Application {
	public static final String SUCCESS_STATUS = "success";
	public static final String FAILED_STATUS = "failed";

	public static void main(String[] args) { // Æô¶¯ÏîÄ¿
		SpringApplication.run(Application.class, args);
	}
}
