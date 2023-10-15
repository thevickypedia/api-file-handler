package main.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import main.api.settings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class APIHandler {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss.SSS a");
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

    public boolean validateRequest(HttpServletRequest request) {
        String datetime = dateFormat.format(new Date());
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        String function = "/" + StackWalker
                .getInstance()
                .walk(stream -> stream.skip(1).findFirst().get())
                .getMethodName().replace('_', '-');
        logger.info("{} accessed '{}' endpoint - '{}'", request.getRemoteAddr(), function, datetime);
        return models.authRequest(request.getHeader("Authorization"));
    }

    @GetMapping(path = "/health")
    public Object health(HttpServletRequest request) {
        if (validateRequest(request)) {
            logger.info("Authorized");
        } else {
            logger.error("401 Unauthorized");
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body("healthy");
    }

    @GetMapping(path = "/download-file")
    public Object download_file(HttpServletRequest request, @RequestParam String fileName) {
        if (validateRequest(request)) {
            logger.info("Authorized");
        } else {
            logger.error("401 Unauthorized");
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        JSONObject outputJSON = new JSONObject();
        File file = Paths.get(settings.sourcePath.toString(), fileName).toFile();
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException error) {
            logger.error(error.toString());
            outputJSON.put("status", String.format("file '%s' doesn't exist in '%s'", fileName, settings.source));
        }
        logger.info(outputJSON.toString());
        return outputJSON.toString();
    }

    @PostMapping(path = "/upload-file", consumes = {"multipart/form-data"})
    public Object upload_file(HttpServletRequest request,
                              @RequestPart MultipartFile file,
                              @RequestParam(required = false) boolean deleteExisting) {
        if (validateRequest(request)) {
            logger.info("Authorized");
        } else {
            logger.error("401 Unauthorized");
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        JSONObject outputJSON = new JSONObject();
        if (file == null) {
            outputJSON.put("status", "no file fragment received");
        } else {
            String fileName = file.getOriginalFilename();
            String fileSize = models.size_converter(file.getSize());
            String fileType = file.getContentType();
            outputJSON.put("name", fileName);
            outputJSON.put("size", fileSize);
            outputJSON.put("type", fileType);
            try {
                byte[] byteArray = file.getBytes();
                if (FileHandler.uploadFile(byteArray, fileName, deleteExisting)) {
                    outputJSON.put("status", String.format("Uploaded '%s' [%s: %s] to '%s'",
                            file.getOriginalFilename(), fileSize, fileType, settings.uploads)
                    );
                } else {
                    outputJSON.put("status", String.format("Failed to upload '%s' [%s: %s] to '%s'",
                            file.getOriginalFilename(), fileSize, fileType, settings.uploads)
                    );
                }
            } catch (IOException error) {
                logger.error(error.toString());
                outputJSON.put("status", error.getMessage());
            }
        }
        logger.info(outputJSON.toString());
        return outputJSON.toString();
    }
}
