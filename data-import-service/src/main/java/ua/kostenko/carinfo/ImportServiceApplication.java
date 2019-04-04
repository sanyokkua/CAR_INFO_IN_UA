package ua.kostenko.carinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImportServiceApplication.class, args);
    }
}
