package com.osudpotro.posmaster.address.district;

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
@Table(name = "districts")
public class District extends  BaseEntity{
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;
}
