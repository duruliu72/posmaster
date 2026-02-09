package com.osudpotro.posmaster.tms.vechile;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {
    private String name;
    private String licenceNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia vehicleDoc;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_driver_id")
    private VehicleDriver vehicleDriver;
}
