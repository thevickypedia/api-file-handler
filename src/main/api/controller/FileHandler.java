package main.api.controller;

import main.api.settings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

    public static Object listFiles() {
        File source = new File(Paths.get(System.getProperty("user.dir"), settings.source).toString());
        List<String> files = new ArrayList<>();
        List<String> directories = new ArrayList<>();
        File[] contentList = source.listFiles();
        try {
            assert contentList != null;
            for (File element : contentList) {
                if (element.isFile()) {
                    files.add(element.getName());
                }
                if (element.isDirectory()) {
                    directories.add(element.getName());
                }
            }
            return ResponseEntity.ok().body(new JSONObject()
                    .put("files", files)
                    .put("directories", directories)
                    .toString());
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
    public static Object downloadFile(String fileName) {
        File file = Paths.get(settings.sourcePath.toString(), fileName).toFile();
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException error) {
            logger.error(error.toString());
            return ResponseEntity.notFound();
        }
    }
}
