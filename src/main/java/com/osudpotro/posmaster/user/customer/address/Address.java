package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String name;
    private String email;
    private String mobile;
    //    1=Home Address ,2=Office Address,3=Hometown
    private Integer addressType;
    //    1=Billing Address ,2=Shipping Address
    private Integer addressCategory;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String locationName;
    private String locationDesc;
    private Boolean isDefault;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "area_id", nullable = true)
    private Area area;
}
