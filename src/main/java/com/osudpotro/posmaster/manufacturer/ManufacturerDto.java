package com.osudpotro.posmaster.manufacturer;
import lombok.Data;

@Data
public class ManufacturerDto {
    private Long id;
    private String name;
    public String alias;
}