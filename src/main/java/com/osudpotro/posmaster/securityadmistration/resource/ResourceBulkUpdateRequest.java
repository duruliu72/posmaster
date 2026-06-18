package com.osudpotro.posmaster.securityadmistration.resource;

import lombok.Data;

import java.util.List;

@Data
public class ResourceBulkUpdateRequest {
    private List<Long> resourceIds;
}
