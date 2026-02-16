package com.osudpotro.posmaster.user.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminUserBulkUpdateRequest {
    private List<Long> adminUserIds;
}
