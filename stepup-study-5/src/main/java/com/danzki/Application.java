package com.danzki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(
   scanBasePackages = {"com.danzki"}
)
public class Application implements CommandLineRunner {
   public static void main(String[] args) {
      ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
   }

   public void run(String... args) throws Exception {
      Thread.currentThread().join();
   }
}
