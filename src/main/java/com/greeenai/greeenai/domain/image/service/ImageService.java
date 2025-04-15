package com.greeenai.greeenai.domain.image.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.image.domain.ContentType;
import com.greeenai.greeenai.domain.image.domain.Image;
import com.greeenai.greeenai.domain.image.domain.ImageType;
import com.greeenai.greeenai.domain.image.repository.ImageRepository;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.property.S3Properties;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;
    private final ImageRepository imageRepository;
    private final S3Properties s3Properties;

    @Transactional
    public Long uploadImage(MultipartFile multipartFile, ImageType imageType, Long targetId) throws IOException {
        validateMultipartFile(multipartFile);

        ContentType contentType = ContentType.from(multipartFile.getContentType());

        String imageKey = ImageKeyGenerator.generate();
        Image image = Image.create(imageType, targetId, imageKey, contentType);
        Image savedImage = imageRepository.save(image);

        try {
            PutObjectRequest request = getPutObjectRequest(savedImage);
            RequestBody rb = getFileRequestBody(multipartFile);
            s3Client.putObject(request, rb);

            return savedImage.getId();

        } catch (S3Exception e) {
            imageRepository.delete(savedImage);
            throw new FileUploadException("S3 파일 업로드 실패", e);
        }
    }

    private PutObjectRequest getPutObjectRequest(Image image) {
        return PutObjectRequest.builder()
                .bucket(s3Properties.getS3().bucket())
                .key(image.generateFileName())
                .contentType(image.getContentType().getValue())
                .build();
    }

    private RequestBody getFileRequestBody(MultipartFile file) throws IOException {
        return RequestBody.fromInputStream(file.getInputStream(), file.getSize());
    }

    private void validateMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException(MULTIPART_FILE_INVALID);
        }
    }
}
