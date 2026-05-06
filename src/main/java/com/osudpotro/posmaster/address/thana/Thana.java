package com.osudpotro.posmaster.address.thana;

import com.osudpotro.posmaster.address.district.District;
import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.common.BaseEntity;
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
@Table(name = "thanas")
public class Thana extends BaseEntity {
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;
}
