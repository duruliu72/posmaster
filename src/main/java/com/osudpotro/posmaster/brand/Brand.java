package com.osudpotro.posmaster.brand;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.picture.Picture;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Brand extends BaseEntity {
    private String name;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private Picture picture;
}