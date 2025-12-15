package com.osudpotro.posmaster.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
//    @Query("SELECT pd FROM ProductDetail pd " +
//            "JOIN pd.size s WHERE pd.product.id = :productId AND (pd.id IN :ids OR pd.parentProductDetail.id IS null) AND s.id <> :sizeId" +
//            " ORDER BY pd.id")
    @Query("SELECT pd FROM ProductDetail pd " +
            "JOIN pd.size s WHERE pd.product.id = :productId AND (pd.id IN :ids OR pd.parentProductDetail.id IS null) AND s.id <> :sizeId" +
            " ORDER BY pd.id DESC")
    List<ProductDetail> findByParentId(@Param("productId") Long productId, @Param("ids") List<Long> ids, @Param("sizeId") Long sizeId);
}
