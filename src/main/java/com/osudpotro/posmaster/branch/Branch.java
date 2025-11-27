package com.osudpotro.posmaster.branch;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Branch extends BaseEntity {
    private String name;
    private String location;
    private String district;
    private double latitude;
    private double longitude;
    private double accuracy;
}
