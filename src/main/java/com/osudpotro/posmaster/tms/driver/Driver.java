package com.osudpotro.posmaster.tms.driver;
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
@Table(name = "tbl_drivers",indexes = {
        @Index(name = "idx_driver_user_id", columnList = "user_id")
})
public class Driver extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_driver_vehicles",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private List<Vehicle> vehicles = new ArrayList<>();
}
