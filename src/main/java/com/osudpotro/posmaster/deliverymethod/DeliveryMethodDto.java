package com.osudpotro.posmaster.deliverymethod;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryMethodDto {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private MultimediaDto media;
}
