package dev.filochowski.springdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDemoApplication {
    public static void main(String[] args) {
        SpringApplication application =
                new SpringApplication(SpringDemoApplication.class);
        application.run(args);
    }
}
