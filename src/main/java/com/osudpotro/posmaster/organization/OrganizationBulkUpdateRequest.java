package com.osudpotro.posmaster.organization;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationBulkUpdateRequest {
    private List<Long> organizationIds;
}
