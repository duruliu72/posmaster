package com.osudpotro.posmaster.action;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateActionRequest {
    @NotBlank(message = "Name is required.")
    private String name;
}