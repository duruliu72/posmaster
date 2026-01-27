package com.osudpotro.posmaster.multimedia;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultimediaCreateRequest {
    private MultipartFile filepond;
}
