package com.osudpotro.posmaster.tag;

import lombok.Data;

import java.util.List;

@Data
public class TagBulkUpdateRequest {
    private List<Long> tagIds;
}
