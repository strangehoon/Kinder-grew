package com.sparta.finalproject.infra.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.finalproject.domain.gallery.entity.Image;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import com.sparta.finalproject.domain.gallery.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    //갤러리 업로드
    public void uploadGalleryImageList(List<MultipartFile> multipartFilelist, String dirName, ImagePost imagePost) throws IOException{
        for (MultipartFile image : multipartFilelist){
            if (image != null){
                String imageUrl = uploadImage(image, dirName);
                imageRepository.save(Image.of(imagePost, imageUrl));
            }
        }
    }

    // 여러 개의 사진 업로드
    public List<String> uploadImageList(List<MultipartFile> imageList, String dirName) throws IOException{
        List<String> imageUrlList = new ArrayList<>(); // 리사이징된 이미지를 저장할 공간
        for (MultipartFile image : imageList){
            imageUrlList.add(uploadImage(image, dirName));
        }
        return imageUrlList;
    }

    private String createFileName(String dirName) {
        return dirName + "/" + UUID.randomUUID();
    }

    public String uploadImage(MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = createFileName(dirName);
        File file = convertMultipartFileToFile(multipartFile, 145);

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        file.delete(); // Delete temporary files
        return amazonS3.getUrl(bucket, fileName).toString();
    }


    private File convertMultipartFileToFile(MultipartFile multipartFile, int width) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        String formatName = multipartFile.getContentType().split("/")[1];
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        BufferedImage originalImage = ImageIO.read(file);
        int originWidth = originalImage.getWidth();
        int originHeight = originalImage.getHeight();
        if(originWidth < width)
            return new File(multipartFile.getOriginalFilename());

        double ratio = (double) originHeight / (double) originWidth;
        int height = (int) Math.round(width * ratio);

        java.awt.Image scaledImage = originalImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        File resizedFile = new File(System.getProperty("java.io.tmpdir") + "/resized_" + multipartFile.getOriginalFilename());
        ImageIO.write(resizedImage, formatName, resizedFile);

        return resizedFile;
    }

    public void deleteFile(String fileName){
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        amazonS3.deleteObject(request);
    }
}
