package com.greeenai.greeenai.domain.image.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.image.domain.ContentType;
import com.greeenai.greeenai.domain.image.domain.Image;
import com.greeenai.greeenai.domain.image.domain.ImageType;
import com.greeenai.greeenai.domain.image.repository.ImageRepository;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.property.S3Properties;
import com.greeenai.greeenai.infra.cloudfront.CloudFrontUrlGenerator;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final ImageRepository imageRepository;
    private final S3Properties s3Properties;

    @Transactional
    public Image uploadImage(MultipartFile multipartFile, ImageType imageType, Long targetId) {
        validateMultipartFile(multipartFile);

        try {
            return saveImage(multipartFile.getBytes(), multipartFile.getContentType(), imageType, targetId);
        } catch (IOException e) {
            throw new CustomException(FILE_READ_FAILED);
        }
    }

    @Transactional
    public Image uploadImage(byte[] imageBytes, String contentType, ImageType imageType, Long targetId) {
        return saveImage(imageBytes, contentType, imageType, targetId);
    }

    @Transactional(readOnly = true)
    public String generateImageDownloadUrl(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getS3().bucket())
                .key(image.generateFileName())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    @Transactional(readOnly = true)
    public String getUrl(Image image) {
        return CloudFrontUrlGenerator.generateUrlByFileName(image.generateFileName());
    }

    private void validateMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException(MULTIPART_FILE_INVALID);
        }
    }

    private Image saveImage(byte[] imageBytes, String contentType, ImageType imageType, Long targetId) {
        ContentType ct = ContentType.from(contentType);
        String imageKey = ImageKeyGenerator.generate();

        Image image = Image.create(imageType, targetId, imageKey, ct);
        Image savedImage = imageRepository.save(image);

        try {
            PutObjectRequest request = getPutObjectRequest(savedImage);
            s3Client.putObject(request, RequestBody.fromBytes(imageBytes));
            return savedImage;
        } catch (S3Exception e) {
            imageRepository.delete(savedImage);
            throw new CustomException(S3_UPLOAD_FAILED);
        }
    }

    private PutObjectRequest getPutObjectRequest(Image image) {
        return PutObjectRequest.builder()
                .bucket(s3Properties.getS3().bucket())
                .key(image.generateFileName())
                .contentType(image.getContentType().getValue())
                .build();
    }
}
