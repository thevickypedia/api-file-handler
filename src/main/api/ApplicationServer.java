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

    public static File create_dir(String dirName) {
        File directory = new File(dirName);
        if (!directory.exists()) {
            boolean ignore = directory.mkdirs();
        }
        return directory;
    }

    public static void main(String[] args) {
        settings.uploadPath = create_dir(settings.uploads);
        settings.sourcePath = create_dir(settings.source);
        if (settings.sourcePath.exists() && settings.uploadPath.exists()) {
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
