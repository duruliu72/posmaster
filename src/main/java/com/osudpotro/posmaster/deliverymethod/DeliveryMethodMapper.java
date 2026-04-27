package com.osudpotro.posmaster.deliverymethod;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMethodMapper {
    //Mapping Here
    //Entity → DTO
    public DeliveryMethodDto toDto(DeliveryMethod dvm) {
        DeliveryMethodDto dvmDto = new DeliveryMethodDto();
        dvmDto.setId(dvm.getId());
        dvmDto.setTitle(dvm.getTitle());
        dvmDto.setMessage(dvm.getMessage());
        dvmDto.setFromDate(dvm.getFromDate());
        dvmDto.setToDate(dvm.getToDate());
        dvmDto.setDefaultDeliveryFee(dvm.getDefaultDeliveryFee());
        dvmDto.setDefaultMinSaleAmountForDeliveryFree(dvm.getDefaultMinSaleAmountForDeliveryFree());
        if(dvm.getMedia()!=null&&dvm.getMedia().getImageUrl()!=null){
            Multimedia multimedia=dvm.getMedia();
            MultimediaDto multimediaDto=new MultimediaDto();
            multimediaDto.setId(multimedia.getId());
            multimediaDto.setName(multimedia.getName());
            multimediaDto.setImageUrl(multimedia.getImageUrl());
            multimediaDto.setMediaType(multimedia.getMediaType());
            multimediaDto.setSourceLink(multimedia.getSourceLink());
            dvmDto.setMedia(multimediaDto);
        }
        return dvmDto;
    }

}
