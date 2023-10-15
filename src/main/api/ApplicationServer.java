package main.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApplicationServer {
    // Only to override defaults if dotenv is loaded
    @SuppressWarnings({"InstantiationOfUtilityClass", "unused"})
    public static settings a = new settings();
    static Logger logger = LoggerFactory.getLogger(ApplicationServer.class);

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

        // Parse as integer only for validation purpose
        Integer port = Integer.parseInt(settings.port);
        logger.info("Starting application on port: http://localhost:{}", port);

        SpringApplication app = new SpringApplication(ApplicationServer.class);
        Map<String, Object> properties = new HashMap<>();
        properties.put("server.port", port);
        properties.put("spring.servlet.multipart.max-file-size", settings.maxSize);
        properties.put("spring.servlet.multipart.max-request-size", settings.maxSize);
        app.setDefaultProperties(properties);
        app.run(args);
    }
}
