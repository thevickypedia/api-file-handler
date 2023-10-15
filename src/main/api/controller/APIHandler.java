package main.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class APIHandler {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss.SSS a");
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

    public static String size_converter(long byteSize) {
        @SuppressWarnings("DuplicatedCode")
        List<String> size_name = Arrays.asList("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB");
        int index = (int) Math.floor(Math.log(byteSize)/ Math.log(1024));
        double byte_zie = byteSize/Math.pow(1024, index);
        return String.format("%s %s", Math.round(byte_zie * 100.0) / 100.0, size_name.get(index));
    }

    @GetMapping(path = "/health")
    public Object upload(HttpServletRequest request) {
        String datetime = dateFormat.format(new Date());
        logger.info("Remote IP {}", request.getRemoteAddr());
        logger.info("Accessed /health endpoint - '{}'", datetime);
        return ResponseEntity.ok().body("healthy");
    }

    @PostMapping(path = "/upload-file", consumes = {"multipart/form-data"})
    public Object upload_file(HttpServletRequest request,
                              @RequestPart MultipartFile file) {
        String datetime = dateFormat.format(new Date());
        logger.info("Remote IP {}", request.getRemoteAddr());
        logger.info("Accessed /upload-file endpoint - '{}'", datetime);
        JSONObject outputJSON = new JSONObject();
        outputJSON.put("timestamp", datetime);
        if (file == null) {
            outputJSON.put("status", "no file fragment received");
        } else {
            outputJSON.put("name", file.getOriginalFilename());
            outputJSON.put("size", size_converter(file.getSize()));
            outputJSON.put("type", file.getContentType());
            outputJSON.put("status", "OK");
        }
        logger.info(outputJSON.toString());
        return outputJSON.toString();
    }
}
