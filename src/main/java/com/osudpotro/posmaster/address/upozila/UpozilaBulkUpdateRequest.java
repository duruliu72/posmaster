package com.osudpotro.posmaster.address.upozila;

import lombok.Data;

import java.util.List;

@Data
public class UpozilaBulkUpdateRequest {
    private List<Long> upozilaIds;
}
