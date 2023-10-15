package main.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class APIHandler {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss.SSS a");
    static Logger logger = LoggerFactory.getLogger(APIHandler.class);

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
            outputJSON.put("size", file.getSize());
            outputJSON.put("type", file.getContentType());
            outputJSON.put("status", "OK");
        }
        logger.info(outputJSON.toString());
        return outputJSON.toString();
    }
}
