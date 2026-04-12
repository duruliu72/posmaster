package com.osudpotro.posmaster.purchase.checked;
import lombok.Data;

@Data
public class CheckedPurchaseRequisitionWithItemPageResponse extends CheckedPurchaseRequisitionDto {
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
