package com.osudpotro.posmaster.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductDetail extends BaseEntity {
    private String productCode;
    private String productDetailCode;
    private String productBarCode;
    private String productDetailBarCode;
    //Stock Keeping Unit
    private String productSku;
    private String productDetailSku;
    private Double regularPrice;
    private Double oldPrice;
    //If no variant then default variant will be NoSize,NoColor
    @ManyToOne(optional = true)
    private VariantUnit size;
    @ManyToOne(optional = true)
    private VariantUnit color;
    private Integer bulkSize;
    private Integer atomQty;
    private Boolean isPublished;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_size_id")
    private VariantUnit parentSize;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_product_detail_id", nullable = true)
    private ProductDetail parentProductDetail;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}
