package com.osudpotro.posmaster.offerhub.offer;

import org.springframework.stereotype.Component;

@Component
public class OfferMapper {
    //Mapping Here
    //Entity → DTO
    public OfferDto toDto(Offer offer) {
        OfferDto offerDto = new OfferDto();
        offerDto.setId(offer.getId());
        offerDto.setName(offer.getName());
        offerDto.setOfferValue(offer.getOfferValue());
        offerDto.setMaxOfferValue(offer.getMaxOfferValue());
        offerDto.setOfferStartDate(offer.getOfferStartDate());
        offerDto.setOfferEndDate(offer.getOfferEndDate());
        return offerDto;
    }
}
