package com.sparta.finalproject.infra.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.finalproject.domain.gallery.entity.Image;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import com.sparta.finalproject.domain.gallery.repository.ImageRepository;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public void uploadGalleryImageList(List<MultipartFile> multipartFilelist, String dirName, ImagePost imagePost) {
        for (MultipartFile image : multipartFilelist){
            if (image != null){
                String imageUrl = uploadImage(image, dirName);
                imageRepository.save(Image.of(imagePost, imageUrl));
            }
        }
    }

    // 여러 개의 사진 업로드
    public List<String> uploadImageList(List<MultipartFile> imageList, String dirName) {
        List<String> imageUrlList = new ArrayList<>(); // 리사이징된 이미지를 저장할 공간
        for (MultipartFile image : imageList){
            imageUrlList.add(uploadImage(image, dirName));
        }
        return imageUrlList;
    }

    //프로필 사진, 유치원 로고 업로드
    public String uploadImage(MultipartFile image, String dirName) {
        String fileName = createFileName(dirName);//중복되지않게 이름을  randomUUID()를 사용해서 생성함
        String fileFormat = image.getContentType().substring(image.getContentType().lastIndexOf("/") + 1); //파일 확장자명 추출
        MultipartFile resizedImage = resizer(fileName, fileFormat, image, 145); //오늘의 핵심 메서드
//========아래부터는 리사이징 된 후 이미지를 S3에다가 업로드하는 방법이다.=========
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resizedImage.getSize()); //사이즈를 전달한다.
        objectMetadata.setContentType(resizedImage.getContentType()); //이미지 타입을 전달한다.
        try (InputStream inputStream = resizedImage.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3Exception(CustomStatusCode.APPROVAL_WAIT);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }
    private String createFileName(String dirName) {
        return dirName + "/" + UUID.randomUUID();
    }

    @Transactional
    public MultipartFile resizer(String fileName, String fileFormat, MultipartFile originalImage, int width) {
        try {
            BufferedImage image = ImageIO.read(originalImage.getInputStream());// MultipartFile -> BufferedImage Convert
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 400보다 작으면 패스
            if(originWidth < width)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", width);
            scale.setAttribute("newHeight", width * originHeight / originWidth);//비율유지를 위해 높이 유지
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormat, baos);
            baos.flush();

            return new CustomMultipartFile(fileName,fileFormat,originalImage.getContentType(), baos.toByteArray());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 줄이는데 실패했습니다.");
        }
    }

    public void deleteFile(String fileName){
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        amazonS3.deleteObject(request);
    }
}
