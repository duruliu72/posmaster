package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionItemTransfer;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.vechile.Vehicle;
import com.osudpotro.posmaster.tms.driver.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private TripStatus tripStatus=TripStatus.PENDING;
    @OneToMany(mappedBy = "vehicleTrip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsOnTrip> goodsOnTrips = new ArrayList<>();
    public String getGeneratedTripRef() {
        String tripPrefix="TRIP";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getTripRef() != null) {
            String lastTripRef = this.getTripRef();
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
        return String.format("%s-%s-%09d",tripPrefix, datePart, nextSeq);
    }
    public int getTotalDelivered() {
        return goodsOnTrips.stream()
                .filter(GoodsOnTrip::isDelivered)
                .toList().size();
    }
}
