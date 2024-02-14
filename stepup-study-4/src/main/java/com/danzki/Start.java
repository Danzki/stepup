package com.danzki;

import com.danzki.config.FileConfig;
import com.danzki.repo.UsersRepo;
import com.danzki.services.FileListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.danzki")
public class Start implements CommandLineRunner {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(Start.class, args);
        var cfg = ctx.getBean(FileConfig.class);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.currentThread().join();
    }
}
