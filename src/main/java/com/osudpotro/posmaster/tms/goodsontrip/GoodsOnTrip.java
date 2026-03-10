package com.osudpotro.posmaster.tms.goodsontrip;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionTransfer;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "goods_on_trips")
public class GoodsOnTrip extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_trip_id", nullable = false)
    private VehicleTrip vehicleTrip;
    @Column(nullable = false, unique = true, length = 50)
    private String goodsRef;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsType goodsType = GoodsType.INVOICE;
    private String goodsReference; // Invoice/GRN/DN number
    private String goodsReferenceDocs;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsStatus goodsStatus = GoodsStatus.LOADED;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "assign_by")
    private User assignBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "received_by")
    private User receivedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_transfer_id")
    private PurchaseRequisitionTransfer purchaseRequisitionTransfer;
    @Column(nullable = false, length = 500)
    private String sourceAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch sourceBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch destBranch;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "source_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "source_longitude")),
            @AttributeOverride(name = "accuracy", column = @Column(name = "source_accuracy"))
    })
    private Location source;
    @Column(nullable = false, length = 500)
    private String destAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dest_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dest_longitude")),
            @AttributeOverride(name = "accuracy", column = @Column(name = "dest_accuracy"))
    })
    private Location destination;
    private String signaturePath;
    @Column(length = 2000)
    private String remarks;
    private LocalDateTime loadedAt;
    private LocalDateTime unLoadedAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "loaded_by")
    private User loadedBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "un_loaded_by")
    private User unLoadedBy;
    // Business logic methods
    public boolean isLoaded() {
        return goodsStatus == GoodsStatus.LOADED;
    }

    public boolean isDelivered() {
        return goodsStatus == GoodsStatus.DELIVERED;
    }

    public void markAsLoaded(User loadedByUser) {
        this.loadedAt = LocalDateTime.now();
        this.loadedBy = loadedByUser;
        this.goodsStatus = GoodsStatus.LOADED;
    }

    public void markAsDelivered(String receivedByPerson, String signaturePath) {
        this.unLoadedAt = LocalDateTime.now();
        this.signaturePath = signaturePath;
        this.goodsStatus = GoodsStatus.DELIVERED;
        this.unLoadedBy = null;
    }

    public String getGeneratedGoodsRef() {
        String tripPrefix = "SL";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getGoodsRef() != null) {
            String lastTripRef = this.getGoodsRef();
            String lastPart = lastTripRef.length() > 8 ? lastTripRef.substring(lastTripRef.length() - 9) : lastTripRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%09d", tripPrefix, datePart, nextSeq);
    }
}
