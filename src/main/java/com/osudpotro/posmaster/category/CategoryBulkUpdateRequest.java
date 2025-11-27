package com.osudpotro.posmaster.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryBulkUpdateRequest {
    private List<Long> catIds;
}
