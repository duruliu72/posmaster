package com.osudpotro.posmaster.user.admin;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailOrMobileValidator.class)
public @interface EmailOrMobileRequired {
    String message() default "Either email or mobile must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
