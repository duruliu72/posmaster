package com.osudpotro.posmaster.securityadmistration.module;

import lombok.Data;

import java.util.List;

@Data
public class ModuleBulkUpdateRequest {
    private List<Long> moduleIds;
}
