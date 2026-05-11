package com.osudpotro.posmaster.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import com.osudpotro.posmaster.offerhub.membership.Membership;
import com.osudpotro.posmaster.offerhub.offer.Offer;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.offerhub.promotion.PromotionOffer;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String saleRef;//Order Ref

    // ✅ ADD THIS FIELD
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod ; //


    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.CUSTOMER;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    Boolean isStoreOut;
    private BigDecimal vatAmount;
    private String billingAddress;
    private String deliveryAddress;
    private BigDecimal reAwardAmount;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "offer_id", nullable = true)
    private Offer offer;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "promotion_offer_id", nullable = true)
    private PromotionOffer promotionOffer;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "membership_id", nullable = true)
    private Membership membership;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_method_id", nullable = true)
    private DeliveryMethod deliveryMethod;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmountForDeliveryFree;
    private String prescriptionDocs;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "special_discount_on_id", nullable = true)
    private Category specialDiscountON;
    private BigDecimal specialDiscount;
    //    1=Through Pos 2=Through Website
    private Integer saleChannel;
    //    1=Pending,2=Processing (After review by customer care) ,3=Accepted by Pharmacy,4=Packaging by Pharmacy,5=Dispatch by Rider(Head)/fleet(head),5=On the way by rider(through Apps),6=Delivered On the way by rider(App),7=Cancelled After review by customer care
//    private Integer saleStatus;
    @OneToOne
    @JoinColumn(name = "sale_status_id", unique = true)
    private SaleStatus saleStatus;
    //    1=Pending,2=Partial,3=Success 4=Credit (For employee due)
    private Integer paymentStatus;
    //    1=Cash On Delivery 2=Partial Paid ,3=Full Paid
    private Integer saleType;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_care_man_id", nullable = true)
    private User customerCareMan;
    private LocalDateTime customerCareAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_man_id", nullable = true)
    private User deliveryMan;
    private LocalDateTime deliveryAt;
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
    @JsonIgnore
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SaleItem> items = new ArrayList<>();
}
