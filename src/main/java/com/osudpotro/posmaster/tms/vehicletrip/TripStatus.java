package com.osudpotro.posmaster.tms.vehicletrip;

public enum TripStatus {
    PENDING,
    SCHEDULED,      // Trip is planned but not yet started
    IN_PROGRESS,    // Trip has started and is ongoing
    COMPLETED,      // Trip finished successfully
    CANCELLED,      // Trip was cancelled before/during execution
    DELAYED         // Trip is behind schedule
}


//ALTER TABLE vehicle_trips
//DROP CONSTRAINT vehicle_trips_trip_status_check;
//
//
//ALTER TABLE vehicle_trips
//ADD CONSTRAINT vehicle_trips_trip_status_check
//CHECK (trip_status IN ('PENDING', 'SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'DELAYED'));