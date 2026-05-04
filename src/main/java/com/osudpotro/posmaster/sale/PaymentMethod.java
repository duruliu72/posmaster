package com.osudpotro.posmaster.sale;

public enum PaymentMethod {
    COD("cod", "Cash On Delivery"),
    WALLET("wallet", "Wallet"),
    SSL("ssl", "Ssl"),
    BKASH("bkash", "Bkash"),
    NAGAD("nagad", "Nagad"),
    ROCKET("rocket", "Rocket"),
    CREDIT("credit","Credit");
    private final String key;
    private final String value;
    PaymentMethod(String code, String description) {
        this.key = code;
        this.value = description;
    }
    public String getCode() {
        return key;
    }

    public String getDescription() {
        return value;
    }

    public static PaymentMethod fromCode(String code) {
        for (PaymentMethod type : values()) {
            if (type.key.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Payment Method code: " + code);
    }
}
