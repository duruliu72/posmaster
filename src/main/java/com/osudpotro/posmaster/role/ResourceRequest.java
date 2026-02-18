package com.osudpotro.posmaster.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ResourceRequest {
    private Long resourceId;
    @JsonProperty("isResourceChecked")
    private boolean isResourceChecked;
    private Set<ActionRequest> actions=new HashSet<>();
}
