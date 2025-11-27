package com.osudpotro.posmaster.producttype;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class ProductType extends BaseEntity {
    // Milk Powder,Liquid,Solid/Snack,Tablet,Capsule etc
    private String name;
}