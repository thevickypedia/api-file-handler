package main.api;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class settings {
    public static String source = "source";
    public static String uploads = "uploads";
    public static String port = "8080";
    public static String maxSize = "10MB";
    public static File uploadPath = null;
    public static File sourcePath = null;
    static Logger logger = LoggerFactory.getLogger(settings.class);
    public static List<String> size_name = Arrays.asList("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB");

    public settings() {
        String env_file = System.getenv().getOrDefault("env_file", ".env");
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("src")
                    .filename(env_file)
                    .load();
            logger.info("Loaded '{}' at '{}'", env_file, "src");
            uploads = dotenv.get("UPLOADS", uploads);
            source = dotenv.get("SOURCE", source);
            port = dotenv.get("PORT", port);
            maxSize = dotenv.get("MAX_SIZE", maxSize);
        } catch (DotenvException error) {
            logger.warn(error.toString());
        }
        for (String s : size_name) {
            if (maxSize.endsWith(s)) {
                return;
            }
        }
        throw new InvalidParameterException(String.format("Size should end with one of %s", size_name));
    }
}
