package com.osudpotro.posmaster.picture;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PictureCreateRequest {
    private MultipartFile filepond;
}