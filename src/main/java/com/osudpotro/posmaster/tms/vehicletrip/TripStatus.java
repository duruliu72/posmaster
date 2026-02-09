package com.osudpotro.posmaster.tms.vehicletrip;

public enum TripStatus {
    SCHEDULED,      // Trip is planned but not yet started
    IN_PROGRESS,    // Trip has started and is ongoing
    COMPLETED,      // Trip finished successfully
    CANCELLED,      // Trip was cancelled before/during execution
    DELAYED         // Trip is behind schedule
}