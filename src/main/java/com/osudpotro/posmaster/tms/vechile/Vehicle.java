package com.osudpotro.posmaster.tms.vechile;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.tms.driver.Driver;
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
@Table(name = "tbl_vehicles")
public class Vehicle extends BaseEntity {
    private String name;
    private String licenceNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia vehicleDoc;
    @ManyToMany(mappedBy = "vehicles")
    private List<Driver> drivers = new ArrayList<>();
}
