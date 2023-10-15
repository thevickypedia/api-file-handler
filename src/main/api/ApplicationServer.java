package main.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collections;

@SpringBootApplication
public class ApplicationServer {
    static Logger logger = LoggerFactory.getLogger(ApplicationServer.class);
    public static settings a = new settings();

    public static boolean create_dir(String dirName) {
        File uploads = new File(dirName);
        if (uploads.exists()) {
            return true;
        } else {
            return uploads.mkdirs();
        }
    }

    public static void main(String[] args) {
        if (create_dir(settings.source) && create_dir(settings.uploads)) {
            logger.info("Created source directory {} and uploads directory {}", settings.source, settings.uploads);
        } else {
            logger.error("Failed to create required directories. Aborting startup.");
            return;
        }

        Integer port = Integer.parseInt(settings.port);
        logger.info("Starting application on port: {}", port);

        SpringApplication app = new SpringApplication(ApplicationServer.class);
        // Parse as integer only for validation purpose
        app.setDefaultProperties(Collections.singletonMap("server.port", port));
        app.run(args);
    }
}
