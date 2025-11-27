package com.osudpotro.posmaster.variantunit;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.varianttype.VariantType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class VariantUnit extends BaseEntity {
    // Small,Medium, Large,Xl,XXl,Standard,PC,Strip,Box,Bottle,Tube,Pot,ML,Red,Green,Blue
    private String name;
    //Size,Color,Thickness,Shape,Purity Etc
    @ManyToOne
    private VariantType variantType;
}