package com.osudpotro.posmaster.inventory;

import lombok.Data;

@Data
public class InventoryFilter {
    private String searchKey;
    private Long branchId;
}
