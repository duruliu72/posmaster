package com.osudpotro.posmaster.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionRequest {
    private Long actionId;
    @JsonProperty("isActionChecked")
    private boolean isActionChecked;
}
