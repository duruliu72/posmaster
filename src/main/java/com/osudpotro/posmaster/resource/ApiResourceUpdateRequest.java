package com.osudpotro.posmaster.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiResourceUpdateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Key is required")
    private String apiResourceKey;
    @NotBlank(message = "Api is required")
    private String apiUrl;
}
