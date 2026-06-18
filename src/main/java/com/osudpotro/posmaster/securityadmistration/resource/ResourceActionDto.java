package com.osudpotro.posmaster.securityadmistration.resource;

import com.osudpotro.posmaster.action.ActionDto;
import lombok.Data;

@Data
public class ResourceActionDto {
    private ActionDto action;
    private Boolean checked;
}
