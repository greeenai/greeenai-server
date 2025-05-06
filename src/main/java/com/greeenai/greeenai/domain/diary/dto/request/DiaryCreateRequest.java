package com.greeenai.greeenai.domain.diary.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class DiaryCreateRequest {

    @NotNull private LocalDate entryDate;

    @Size(max = 3)
    private List<MultipartFile> photos;
}
