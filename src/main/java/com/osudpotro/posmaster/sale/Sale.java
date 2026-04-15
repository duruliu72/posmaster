package com.osudpotro.posmaster.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String saleInvoice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id", nullable = true)
    private User customer;
    private String salePoint;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sale_point_man_id", nullable = true)
    private User salePointMan;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sale_man_id", nullable = true)
    private User saleMan;
    Boolean isStoreOut;
    BigDecimal offerMinOrderValue;
    private BigDecimal vatAmount;
    private Integer saleStatus;
    private Integer saleType;
    private Integer paymentStatus;
    String billingAddress;
    String deliveryAddress;
//    1=Through Pos 2=Through Website
    private Integer saleChannel;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_method_id", nullable = true)
    private DeliveryMethod deliveryMethod;
//    private BigDecimal deliveryFee;
//    private BigDecimal minSaleAmountForDeliveryFee;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_man_id", nullable = true)
    private User deliveryMan;
    private LocalDateTime deliveryAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_care_man_id", nullable = true)
    private User customerCareMan;
    private LocalDateTime customerCareAt;
    private String prescriptionDocs;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

}
