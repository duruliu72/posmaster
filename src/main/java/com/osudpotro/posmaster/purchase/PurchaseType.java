package com.osudpotro.posmaster.purchase;

public enum PurchaseType {
    COMPANY_PURCHASE("cp", "Company Purchase"),
    LOCAL_PURCHASE("lp", "Local Purchase"),
    PURCHASE_ORDER("po", "Purchase Order"),
    PROCUREMENT("procurement", "Procurement");
    private final String key;
    private final String value;
    PurchaseType(String code, String description) {
        this.key = code;
        this.value = description;
    }
    public String getCode() {
        return key;
    }
    public String getDescription() {
        return value;
    }
    public static PurchaseType fromCode(String code) {
        for (PurchaseType type : values()) {
            if (type.key.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PurchaseType code: " + code);
    }
}
