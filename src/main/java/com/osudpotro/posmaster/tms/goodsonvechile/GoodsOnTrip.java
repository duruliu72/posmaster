package com.osudpotro.posmaster.tms.goodsonvechile;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsType goodsType = GoodsType.INVOICE;
    private String goodsReference; // Invoice/GRN/DN number
    private String goodsReferenceDocs;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsStatus goodsStatus = GoodsStatus.LOADED;
    @Column(nullable = false, length = 500)
    private String sourceAddress;
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
    private LocalDateTime loadedTime;
    private LocalDateTime unloadedTime;
    @Column(length = 500)
    private String receivedBy;
    private String signaturePath;
    @Column(length = 2000)
    private String remarks;
    private LocalDateTime loadedAt;
    private LocalDateTime unloadedAt;
    @Column(length = 500)
    private String loadedBy;
    @Column(length = 500)
    private String unloadedBy;
    // Business logic methods
    public boolean isLoaded() {
        return goodsStatus == GoodsStatus.LOADED;
    }

    public boolean isDelivered() {
        return goodsStatus == GoodsStatus.DELIVERED;
    }

    public void markAsLoaded(String loadedByUser) {
        this.loadedAt = LocalDateTime.now();
        this.loadedBy = loadedByUser;
        this.goodsStatus = GoodsStatus.LOADED;
    }
    public void markAsDelivered(String receivedByPerson, String signaturePath) {
        this.unloadedAt = LocalDateTime.now();
        this.receivedBy = receivedByPerson;
        this.signaturePath = signaturePath;
        this.goodsStatus = GoodsStatus.DELIVERED;
        this.unloadedBy = "Driver"; // Or capture from context
    }
}
