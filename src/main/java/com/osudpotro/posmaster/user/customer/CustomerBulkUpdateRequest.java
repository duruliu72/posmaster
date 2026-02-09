package com.osudpotro.posmaster.user.customer;

import lombok.Data;

import java.util.List;

@Data
public class CustomerBulkUpdateRequest {
    private List<Long> customerIds;
}
