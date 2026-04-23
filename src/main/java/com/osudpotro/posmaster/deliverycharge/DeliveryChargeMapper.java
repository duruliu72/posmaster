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
    public DeliveryChargeDto toDto(DeliveryCharge entity) {
        if (entity == null) {
            return null;
        }
        DeliveryChargeDto dvcTo = new DeliveryChargeDto();
        dvcTo.setId(entity.getId());
        DeliveryMethodDto dmDto = new DeliveryMethodDto();
        dmDto.setTitle(entity.getDeliveryMethod().getTitle());
        dmDto.setMessage(entity.getDeliveryMethod().getMessage());
        dvcTo.setDeliveryMethod(dmDto);
        dvcTo.setDeliveryFee(entity.getDeliveryFee());
        dvcTo.setMinSaleAmountForDeliveryFree(entity.getMinSaleAmountForDeliveryFree());
        dvcTo.setDeliveryFee(entity.getDeliveryFee());
        dvcTo.setChargeBasedOn(entity.getChargeBasedOn());
        dvcTo.setMinDistance(entity.getMinDistance());
        if (entity.getArea() != null) {
            AreaDto areaDto = areaMapper.toDto(entity.getArea());
            dvcTo.setArea(areaDto);
        }
        dvcTo.setIsFree(entity.getIsFree());
        dvcTo.setIsActive(entity.getIsActive());
        return dvcTo;
    }
}