package com.osudpotro.posmaster.tms.vehicledriver;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
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
@Table(name = "vehicle_drivers")
public class VehicleDriver extends BaseEntity {
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = true)
    private String email;
    @Column(unique = true, nullable = true)
    private String mobile;
    private Boolean isValidEmail = true;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    //    like Male=1, Female=2, Third=3
    private Integer gender;
    private String provider;
    private String providerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia profilePic;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @OneToMany(mappedBy = "vehicleDriver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();
}
