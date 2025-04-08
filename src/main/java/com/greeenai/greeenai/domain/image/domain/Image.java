package com.greeenai.greeenai.domain.image.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private Long targetId;

    private String imageKey;

    @Enumerated(EnumType.STRING)
    private FileExtension fileExtension;

    @Builder(access = AccessLevel.PRIVATE)
    private Image(ImageType imageType, Long targetId, String imageKey, FileExtension fileExtension) {
        this.imageType = imageType;
        this.targetId = targetId;
        this.imageKey = imageKey;
        this.fileExtension = fileExtension;
    }

    public static Image create(ImageType imageType, Long targetId, String imageKey, FileExtension fileExtension) {
        return Image.builder()
                .imageType(imageType)
                .targetId(targetId)
                .imageKey(imageKey)
                .fileExtension(fileExtension)
                .build();
    }
}
