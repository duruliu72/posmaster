package com.osudpotro.posmaster.purchase.transfer;
import lombok.Data;

@Data
public class PurchaseRequisitionTransferWithItemPageResponse extends PurchaseRequisitionTransferDto {
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
