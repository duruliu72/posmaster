package com.osudpotro.posmaster.purchase.checked;

import lombok.Data;

import java.util.List;

@Data
public class AddToInventoryRequest {
    private List<UpdateFromBranchRequest> items;
}
