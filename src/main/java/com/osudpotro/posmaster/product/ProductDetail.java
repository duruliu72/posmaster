package com.osudpotro.posmaster.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayOutputStream;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductDetail extends BaseEntity {
    private String productDetailCode;
    private String productDetailBarCode;
    //Stock Keeping Unit
    private String productDetailSku;
    private Double regularPrice;
    private Double oldPrice;
    private Double purchasePrice;
    //If no variant then default variant will be NoSize,NoColor
    @ManyToOne(optional = true)
    private VariantUnit size;
    @ManyToOne(optional = true)
    private VariantUnit color;
    private Integer bulkSize;
    private Integer atomQty;
    private Boolean isPublished;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_product_detail_id", nullable = true)
    private ProductDetail parentProductDetail;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    public byte[] generateProductDetailsBarCodeImage(Integer customHeight, Integer customWidth) {
        int width = customWidth != null ? customWidth : 400;
        int height = customHeight != null ? customHeight : 150;
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(this.productDetailBarCode, BarcodeFormat.CODE_128, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }
}
