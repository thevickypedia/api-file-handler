package main.api.controller;

import main.api.settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

    public static Object listFiles() {
        File source = new File(Paths.get(System.getProperty("user.dir"), settings.source).toString());
        List<String> output = new ArrayList<>();
        File[] fileList = source.listFiles();
        try {
            assert fileList != null;
            for (File file : fileList) {
                if (file.isFile()) {
                    output.add(file.getName());
                }
            }
            return ResponseEntity.ok().body(output);
        } catch (NullPointerException error) {
            logger.error(error.getMessage());
            return ResponseEntity.internalServerError().body(error.getMessage());
        }
    }

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
