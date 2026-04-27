package com.osudpotro.posmaster.offerhub.promotion;

import com.osudpotro.posmaster.offerhub.offer.Offer;
import com.osudpotro.posmaster.offerhub.offer.OfferDto;
import org.springframework.stereotype.Component;

@Component
public class PromotionOfferMapper {
    //Mapping Here
    //Entity → DTO
    public PromotionOfferDto toDto(PromotionOffer promoOffer) {
        PromotionOfferDto promoOfferDto = new PromotionOfferDto();
        promoOfferDto.setId(promoOffer.getId());
//        promoOfferDto.setName(offer.getName());
//        promoOfferDto.setOfferValue(offer.getOfferValue());
//        promoOfferDto.setMaxOfferValue(offer.getMaxOfferValue());
//        promoOfferDto.setOfferStartDate(offer.getOfferStartDate());
//        promoOfferDto.setOfferEndDate(offer.getOfferEndDate());
        return promoOfferDto;
    }
}
