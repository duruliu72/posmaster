package com.osudpotro.posmaster.sale;

public enum PaymentOption {
    COD("cod", "Cash On Delivery"),
    CASH("cash", "Cash On Hand");
    private final String key;
    private final String value;
    PaymentOption(String code, String description) {
        this.key = code;
        this.value = description;
    }

    public String getCode() {
        return key;
    }

    public String getDescription() {
        return value;
    }

    public static PaymentOption fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return COD; // default
        }
        for (PaymentOption type : values()) {
            if (type.key.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Payment Option code: " + code);
    }
}