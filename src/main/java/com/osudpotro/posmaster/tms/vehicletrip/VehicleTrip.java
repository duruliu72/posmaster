package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.tms.goodsonvechile.GoodsOnTrip;
import com.osudpotro.posmaster.tms.vechile.Vehicle;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "vehicle_trips")
public class VehicleTrip extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String tripRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_driver_id")
    private VehicleDriver vehicleDriver;
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
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
    private TripStatus tripStatus=TripStatus.SCHEDULED;
    @OneToMany(mappedBy = "vehicleTrip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsOnTrip> goodsItems = new ArrayList<>();
}
