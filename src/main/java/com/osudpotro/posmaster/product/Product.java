package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.producttype.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Product extends BaseEntity {
    private String productName;
    private String productCode;
    private String productBarCode;
    //Stock Keeping Unit
    private String productSku;
    private Double productRegularPrice;
    @Column(nullable = true)
    private Double productOldPrice;
    private String description;
    @ManyToOne
    private Manufacturer manufacturer;
    @ManyToOne
    private Brand brand;
//  Milk Powder,Tablet,Capsule etc
    @ManyToOne
    private Category category;
    @ManyToOne
    private ProductType productType;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductGeneric> productGenerics =new ArrayList<>();
}