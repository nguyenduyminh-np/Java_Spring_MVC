package vn.hoidanit.laptopshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    public List<String> handleSaveUploadFile(MultipartFile[] files, String targetFolder) {
        String rootPath = this.servletContext.getRealPath("/resources/images");
        List<String> finalNames = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                byte[] bytes;
                bytes = file.getBytes();

                File dir = new File(rootPath + File.separator + "avatar");
                // Check directory "avatar" if exists
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                finalNames.add(finalName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return finalNames;
    }
}