package com.osudpotro.posmaster.tms.goodsonvechile;

enum GoodsStatus {
    PENDING,        // Not yet loaded
    LOADED,         // Loaded on vehicle
    IN_TRANSIT,     // Currently in transit
    DELIVERED,      // Successfully delivered
    PARTIALLY_DELIVERED, // Partial delivery
    DAMAGED,        // Goods damaged
    LOST,           // Goods lost
    RETURNED,       // Returned to sender
    HELD_AT_CUSTOMS, // Held at customs
    QUARANTINED     // Under quarantine
}