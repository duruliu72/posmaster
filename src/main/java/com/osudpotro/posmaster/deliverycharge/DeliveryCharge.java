package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.address.city.City;
import com.osudpotro.posmaster.address.district.District;
import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.address.thana.Thana;
import com.osudpotro.posmaster.address.upozila.Upozila;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "delivery_charges")
public class DeliveryCharge extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;
    private BigDecimal deliveryFee;
    private BigDecimal minSaleAmountForDeliveryFree;
    //    1=For Distance,2=for Area,3=for Division,4=For District,5=For Thana,6=For Upozila,7= For City,8= For State
    private Integer chargeBasedOn;
    //    Min Distance From Nearest Branch
    private Double minDistance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;
    private Boolean isFree = false;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_delivery_charge_id", nullable = true)
    private DeliveryCharge parentDeliveryCharge;
    private Boolean isActive=true;
}
