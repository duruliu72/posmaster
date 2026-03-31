package com.osudpotro.posmaster.purchase.transfer;

import lombok.Data;

@Data
public class UpdateFromMedicineCornerRequest {
    private Long purchaseRequisitionItemTransferId;
    private String productionBatchNo;
    private String manufactureDate;
    private String expiredDate;
}
