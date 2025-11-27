package com.osudpotro.posmaster.varianttype;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class VariantType extends BaseEntity {
    //Size,Color,Thickness,Shape,Purity Etc
    private String name;
}


//public enum VariantType {
//    Size,
//    Color
//}