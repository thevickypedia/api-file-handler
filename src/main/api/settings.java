package main.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class settings {
    static Logger logger = LoggerFactory.getLogger(settings.class);
    static File dotenvPath = new File("src");
    static File dotenvFile = new File(".env");
    static String source = "source";
    static String uploads = "uploads";
    static String port = "8080";

    public settings() {
        if (dotenvPath.exists() && dotenvFile.exists()) {
            logger.info("Loading {} at {}", dotenvFile, dotenvPath);
            Dotenv dotenv = Dotenv.configure()
                    .directory(dotenvPath.toString())
                    .filename(dotenvFile.toString())
                    .load();
            uploads = dotenv.get("UPLOADS", uploads);
            source = dotenv.get("SOURCE", source);
            port = dotenv.get("PORT", port);
        } else {
            logger.info("{} was not found in {}", dotenvFile, dotenvPath);
        }
    }
}
