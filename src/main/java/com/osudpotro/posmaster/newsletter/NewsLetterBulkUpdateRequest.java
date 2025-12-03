package com.osudpotro.posmaster.newsletter;

import lombok.Data;

import java.util.List;

@Data
public class NewsLetterBulkUpdateRequest {
    private List<Long> newsletterIds;

}
