package com.osudpotro.posmaster.resource.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiResourceCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Key is required")
    private String apiResourceKey;
    @NotBlank(message = "Api is required")
    private String apiUrl;
}
