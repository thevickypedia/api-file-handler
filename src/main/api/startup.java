package main.api;

import java.io.File;

public class startup {

    public static boolean create_dir(String dirName) {
        File uploads = new File(dirName);
        if (uploads.exists()) {
            return true;
        } else {
            return uploads.mkdirs();
        }
    }

    public static void main(String[] args) {
        boolean sourceDir = create_dir("source");
        boolean uploadDir = create_dir("uploads");
        assert sourceDir == uploadDir == true;
    }
}
