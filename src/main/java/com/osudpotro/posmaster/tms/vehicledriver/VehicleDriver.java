package com.osudpotro.posmaster.tms.vehicledriver;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.tms.vechile.Vehicle;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "vehicle_drivers",indexes = {
        @Index(name = "idx_vehicle_driver_user_id", columnList = "user_id")
})
public class VehicleDriver extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @OneToMany(mappedBy = "vehicleDriver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();
}
