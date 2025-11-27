package com.osudpotro.posmaster.variantunit;

import com.osudpotro.posmaster.varianttype.VariantType;
import com.osudpotro.posmaster.varianttype.VariantTypeDto;
import org.springframework.stereotype.Component;

@Component
public class CustomVariantUnitMapper {
    //Mapping Here
    //Entity → DTO
    public VariantUnitDto toDto(VariantUnit variantUnit) {
        VariantUnitDto variantUnitDto=new VariantUnitDto();
        variantUnitDto.setId(variantUnit.getId());
        variantUnitDto.setName(variantUnit.getName());
        if(variantUnit.getVariantType()!=null){
            VariantTypeDto variantType = new VariantTypeDto();
            variantType.setId(variantUnit.getVariantType().getId());
            variantType.setName(variantUnit.getVariantType().getName());
            variantUnitDto.setVariantType(variantType);
        }
        return variantUnitDto;
    }
}
