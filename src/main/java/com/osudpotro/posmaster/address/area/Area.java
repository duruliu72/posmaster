package com.osudpotro.posmaster.address.area;

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
@Table(name = "areas")
public class Area  extends BaseEntity {
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_area_id", nullable = true)
    private Area parentArea;
    private Boolean isSubArea=false;
}
