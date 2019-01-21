package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//note: flag for spring boot
@SpringBootApplication
public class HelloWorldDemoApplication {
//    main
    public static void main(String[] args){
        SpringApplication.run(HelloWorldDemoApplication.class, args);
    }
}
