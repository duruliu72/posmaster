package com.osudpotro.posmaster.resource.ui;

import lombok.Data;

import java.util.List;

@Data
public class UiResourceBulkUpdateRequest {
    private List<Long> uiResourceIds;
}
