package com.osudpotro.posmaster.branch;

import lombok.Data;

import java.util.List;
@Data
public class BranchBulkUpdateRequest {
    private List<Long> branchIds;
}
