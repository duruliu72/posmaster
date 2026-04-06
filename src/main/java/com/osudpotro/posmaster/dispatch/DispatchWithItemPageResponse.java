package com.osudpotro.posmaster.dispatch;

import lombok.Data;

@Data
public class DispatchWithItemPageResponse  extends DispatchDto{
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
