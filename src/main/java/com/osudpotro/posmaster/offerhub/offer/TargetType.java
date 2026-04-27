package com.osudpotro.posmaster.offerhub.offer;

public enum TargetType {
    PRODUCT(1, "Product"),
    CATEGORY(2, "Category"),
    MANUFACTURER(3, "Manufacturer"),
    PRODUCT_TYPE(4, "Product Type"),
    AREA(5, "Area");
    private final int key;
    private final String value;

    TargetType(int key, String displayName) {
        this.key = key;
        this.value = displayName;
    }

    public int getCode() {
        return key;
    }

    public String getDescription() {
        return value;
    }

    public static TargetType fromCode(int code) {
        for (TargetType type : values()) {
            if (type.key == (code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Target Type code: " + code);
    }
}
