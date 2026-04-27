package com.osudpotro.posmaster.offerhub.membership;

import lombok.Data;

import java.util.List;

@Data
public class MembershipBulkUpdateRequest {
    private List<Long> membershipIds;
}
