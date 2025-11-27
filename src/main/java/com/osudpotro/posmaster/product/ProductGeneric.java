package com.osudpotro.posmaster.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.generic.Generic;
import com.osudpotro.posmaster.genericunit.GenericUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductGeneric extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generic_id")
    private Generic generic;
    private Double dose;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generic_unit_id")
    private GenericUnit genericUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}