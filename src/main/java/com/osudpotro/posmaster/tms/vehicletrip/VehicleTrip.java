package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.tms.goodsonvechile.GoodsOnTrip;
import com.osudpotro.posmaster.tms.vechile.Vehicle;
import com.osudpotro.posmaster.tms.driver.Driver;
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
    @JoinColumn(name = "driver_id")
    private Driver driver;
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus tripStatus=TripStatus.SCHEDULED;
    @OneToMany(mappedBy = "vehicleTrip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsOnTrip> goodsItems = new ArrayList<>();
}
