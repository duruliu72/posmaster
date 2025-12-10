package com.osudpotro.posmaster.resource.ui;

import com.osudpotro.posmaster.action.ActionDto;
import lombok.Data;

@Data
public class UiResourceActionDto {
    private ActionDto action;
    private Boolean checked;
}
