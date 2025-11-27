package com.osudpotro.posmaster.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductGenericCreateRequest {
    @NotNull(message = "Generic is required")
    private Long genericId;
    private Double dose;
    @NotNull(message = "Generic Unit is required")
    private Long genericUnitId;
}