package com.osudpotro.posmaster.warehouse;

import lombok.Data;

@Data
public class WarehouseDto {
    private String name;
    private String location;
    private String address;
    private String district;
    private String country;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String mobile;
}
