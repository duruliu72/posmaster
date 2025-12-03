package com.osudpotro.posmaster.resource;

import com.osudpotro.posmaster.action.ActionDto;
import lombok.Data;

@Data
public class UiResourceActionDto {
    private ActionDto action;
    private Boolean checked;
}
