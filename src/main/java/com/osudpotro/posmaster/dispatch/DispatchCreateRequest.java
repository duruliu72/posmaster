package com.osudpotro.posmaster.dispatch;

import lombok.Data;

@Data
public class DispatchCreateRequest {
    private Long requestReceivedBranchId;
    private String note;
}
