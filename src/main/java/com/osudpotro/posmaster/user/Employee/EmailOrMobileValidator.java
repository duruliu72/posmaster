package com.osudpotro.posmaster.user.Employee;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class EmailOrMobileValidator implements ConstraintValidator<EmailOrMobileRequired, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Class<?> clazz = value.getClass();
            // Get email field
            Field emailField = clazz.getDeclaredField("email");
            emailField.setAccessible(true);
            String email = (String) emailField.get(value);
            // Get mobile field
            Field mobileField = value.getClass().getDeclaredField("mobile");
            mobileField.setAccessible(true);
            String mobile = (String) mobileField.get(value);
            // Check if at least one is provided and not empty
            boolean hasValidEmail = email != null && !email.trim().isEmpty();
            boolean hasValidMobile = mobile != null && !mobile.trim().isEmpty();
            if (!hasValidEmail && !hasValidMobile) {
                // Customize the error message
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Please provide either email or mobile number")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
