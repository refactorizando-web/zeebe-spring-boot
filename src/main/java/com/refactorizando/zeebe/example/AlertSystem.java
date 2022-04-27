package com.refactorizando.zeebe.example;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class AlertSystem {

  public static void main(String[] args) {
    SpringApplication.run(AlertSystem.class, args);
  }


}
