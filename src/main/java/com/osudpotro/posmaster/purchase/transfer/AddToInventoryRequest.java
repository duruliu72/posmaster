package com.osudpotro.posmaster.purchase.transfer;

import lombok.Data;

import java.util.List;

@Data
public class AddToInventoryRequest {
    private List<UpdateFromMedicineCornerRequest> items;
}
