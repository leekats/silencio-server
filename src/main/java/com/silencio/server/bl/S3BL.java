package com.silencio.server.bl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3BL {

    @Value("${s3.addface:silencio-faces}")
    private String FACE_BUCKET;
    final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

    public boolean saveImageToS3(String personId, MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartToFile(multipartFile);
            uploadFileToS3Bucket(file, personId);
            file.delete();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void removeImage(String personId) {
        s3.deleteObject(FACE_BUCKET, personId + ".jpg");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void uploadFileToS3Bucket(File file, String filename) {
        final PutObjectRequest putObjectRequest = new PutObjectRequest(FACE_BUCKET, filename, file);
        s3.putObject(putObjectRequest);
    }
}
