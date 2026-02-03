package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.multimedia.Multimedia;
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
@Table(name = "products")
public class Product extends BaseEntity {
    private String productName;
    private String productCode;
    private String productBarCode;
    //Stock Keeping Unit
    private String productSku;
    private String tags;
    private Double productRegularPrice;
    @Column(nullable = true)
    private Double productOldPrice;
    @Lob
    private String description;
    private Boolean isPrescribeNeeded;
    private String seoPageName;
    private String metaTitle;
    private String metaKeywords;
    @Lob
    private String metaDescription;
    @ManyToOne
    private Manufacturer manufacturer;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private Category category;
    //  Milk Powder,Tablet,Capsule etc
    @ManyToOne
    private ProductType productType;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @SQLRestriction("status = 1")
    private List<ProductGeneric> productGenerics = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia media;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ProductDetail> details = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_product_unit_id")
    private ProductDetail purchaseProductUnit;
//    public ProductDetail getProductDetailForPurchase() {
//        if (details == null || details.isEmpty()) {
//            return null;
//        }
//        if (details.size() == 1) {
//            return details.get(0);
//        } else {
//            return findSelectedItem(details.get(0));
//        }
//    }
//
//    private ProductDetail findSelectedItem(ProductDetail root) {
//        ProductDetail item = details.stream()
//                .filter(pd -> {
//                    return pd.getParentProductDetail() != null && pd.getParentProductDetail().getId().equals(root.getId());
//                })
//                .findFirst()
//                .orElse(null);
//        if (item == null) {
//            return root;
//        }
//        return findSelectedItem(item);
//    }
}