package main.api.controller;

import main.api.settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

    public static boolean uploadFile(byte[] fileData, String fileName, Boolean overwrite)
            throws FileAlreadyExistsException {
        logger.info(String.format("flag for deletion: %s", overwrite));
        Path target = Paths.get(settings.uploadPath.toString(), fileName);
        if (target.toFile().exists()) {
            if (!overwrite) {
                throw new FileAlreadyExistsException(String.format(
                        "'%s' already exists in '%s' directory. Use 'overwrite' flag to replace existing file.",
                        fileName, settings.uploads
                ));
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(target.toFile())) {
            fileOutputStream.write(fileData);
            fileOutputStream.flush();  // write to buffer immediately
            logger.info("Wrote to file successfully.");
            return true;
        } catch (IOException error) {
            logger.error(error.toString());
            return false;
        }
    }
}
