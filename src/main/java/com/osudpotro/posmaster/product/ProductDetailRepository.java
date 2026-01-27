package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.variantunit.VariantUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaSpecificationExecutor<ProductDetail>, JpaRepository<ProductDetail, Long> {
    boolean existsByProductAndColor(Product product, VariantUnit color);

    //    Optional<ProductDetail> findByProductAndColor(Product product, VariantUnit color);
//    @Query("SELECT pd FROM ProductDetail pd " +
//            "JOIN pd.size s WHERE pd.product.id = :productId AND (pd.id IN :ids OR pd.parentProductDetail.id IS null) AND s.id <> :sizeId" +
//            " ORDER BY pd.id")
    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.id = :productId AND (pd.id =:ppdId OR (pd.parentProductDetail.id IS null AND pd.id NOT IN :fPPdId))" +
            " ORDER BY pd.id ASC")
    List<ProductDetail> findByParentId(@Param("productId") Long productId, @Param("ppdId") Long ppdId, @Param("fPPdId") Long firstParentProductDetailId);

    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.id = :productId " +
            "AND (pd.id NOT IN (SELECT upd.parentProductDetail.id FROM ProductDetail upd WHERE upd.product.id = :productId AND upd.parentProductDetail.id IS NOT NULL) AND pd.id <> :pdId ) " +
            "OR pd.id=:ppdId" +
            " ORDER BY pd.id ASC")
    List<ProductDetail> findProductDetailsByParent(@Param("productId") Long productId, @Param("pdId") Long pdId, @Param("ppdId") Long ppdId);

    Optional<ProductDetail> findByProductAndSizeAndColor(Product product, VariantUnit size, VariantUnit color);

    ProductDetail findTopByOrderByCreatedAtDesc();

    @Query("""
                SELECT pd FROM ProductDetail pd
                JOIN pd.product p
                  LEFT JOIN p.category c
                  LEFT JOIN p.brand b
                  LEFT JOIN p.manufacturer m
                  LEFT JOIN p.productType pt
                WHERE LOWER(pd.productDetailCode) LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(pd.productDetailBarCode) LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(pd.productDetailSku) LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(c.name)                  LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(b.name)                  LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(m.name)                  LIKE LOWER(CONCAT('%', :key, '%'))
                   OR LOWER(pt.name)                 LIKE LOWER(CONCAT('%', :key, '%'))
            """)
    Page<ProductDetail> searchByAnyFields(
            @Param("key") String key,
            Pageable pageable
    );
}
