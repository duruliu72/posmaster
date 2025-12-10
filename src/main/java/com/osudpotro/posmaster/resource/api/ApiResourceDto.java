package com.osudpotro.posmaster.resource.api;

import lombok.Data;

@Data
public class ApiResourceDto {
    private Long id;
    private String name;
    private String apiResourceKey;
    private String apiUrl;
}
