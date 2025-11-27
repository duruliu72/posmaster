package com.osudpotro.posmaster.manufacturer;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Manufacturer extends BaseEntity {
    public String name;
    public String alias;
}