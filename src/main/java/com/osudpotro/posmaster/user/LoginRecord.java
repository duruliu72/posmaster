package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_records")
public class LoginRecord extends BaseEntity {
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "device_brand")
    private String deviceBrand;// Mobile only
    @Column(name = "device_model")
    private String deviceModel; // Mobile only
}
