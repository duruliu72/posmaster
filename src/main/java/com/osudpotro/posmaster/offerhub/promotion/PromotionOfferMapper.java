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
        promoOfferDto.setName(promoOffer.getName());
        promoOfferDto.setPromoCode(promoOffer.getPromoCode());
        promoOfferDto.setAlias(promoOffer.getAlias());
        promoOfferDto.setDesc(promoOfferDto.getDesc());
        promoOfferDto.setPromotionValue(promoOffer.getPromotionValue());
        promoOfferDto.setMinOrderValue(promoOffer.getMinOrderValue());
        promoOfferDto.setPromoStartDate(promoOffer.getPromoStartDate());
        promoOfferDto.setPromoEndDate(promoOffer.getPromoEndDate());
        return promoOfferDto;
    }
}
