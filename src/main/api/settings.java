package main.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class settings {
    static Logger logger = LoggerFactory.getLogger(settings.class);
    static String source = "source";
    static String uploads = "uploads";
    static String port = "8080";

    public settings() {
        String env_file = System.getenv().getOrDefault("env_file", ".env");
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dotenvFile = Paths.get(currentPath.toString(), "src", env_file);
        File dotENV = new File(dotenvFile.toString());
        if (dotENV.exists()) {
            logger.info("Loading '{}' at '{}'", dotenvFile, "src");
            Dotenv dotenv = Dotenv.configure()
                    .directory("src")
                    .filename(env_file)
                    .load();
            uploads = dotenv.get("UPLOADS", uploads);
            source = dotenv.get("SOURCE", source);
            port = dotenv.get("PORT", port);
        } else {
            logger.info("'{}' was not found in '{}'", dotenvFile, "src");
        }
    }
}
