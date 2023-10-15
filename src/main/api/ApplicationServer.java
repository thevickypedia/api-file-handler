package main.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationServer {
    public static void main(String [] args){
        SpringApplication app = new SpringApplication(ApplicationServer.class);
        app.run(args);
    }
}
