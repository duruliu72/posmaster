package com.osudpotro.posmaster.purchase.requisition;
import lombok.Data;
@Data
public class PurchaseRequisitionWithItemPageResponse extends  PurchaseRequisitionDto{
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
