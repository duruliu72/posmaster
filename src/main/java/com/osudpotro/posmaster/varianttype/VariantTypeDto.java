package com.osudpotro.posmaster.varianttype;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VariantTypeDto {
    private Long id;
    private String name;
    private LocalDate createdAt;
    private  LocalDate updatedAt;
}
