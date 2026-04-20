package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.address.area.AreaDto;
import com.osudpotro.posmaster.address.area.AreaMapper;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeliveryChargeMapper {
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private BasedOnEntityResolverService basedOnEntityResolverService;

    public DeliveryChargeDto toDto(DeliveryCharge entity) {
        if (entity == null) {
            return null;
        }
        DeliveryChargeDto dto = new DeliveryChargeDto();
        DeliveryMethodDto dmDto = new DeliveryMethodDto();
        dmDto.setTitle(entity.getDeliveryMethod().getTitle());
        dmDto.setMessage(entity.getDeliveryMethod().getMessage());
        dto.setDeliveryMethod(dmDto);
        dto.setDeliveryFee(entity.getDeliveryFee());
        dto.setMinSaleAmountForDeliveryFree(entity.getMinSaleAmountForDeliveryFree());
        dto.setDeliveryFee(entity.getDeliveryFee());
        dto.setChargeBasedOn(entity.getChargeBasedOn());
        dto.setMinDistance(entity.getMinDistance());
        if (entity.getArea() != null) {
            AreaDto areaDto = areaMapper.toDto(entity.getArea());
            dto.setArea(areaDto);
        }
//        dto.setBasedOnEntityId(entity.getBasedOnEntityId());
//        dto.setBasedOnEntityName(basedOnEntityResolverService.getBasedOnEntityName(entity));
        dto.setIsFree(entity.getIsFree());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}