package com.osudpotro.posmaster.user;

public enum UserType {
    ADMIN,
    EMPLOYEE,
    CUSTOMER,
    VEHICLE_DRIVER,
    SUPPLIER,
    PARTNER
}


//ALTER TABLE users DROP CONSTRAINT IF EXISTS users_user_type_check;
//ALTER TABLE users ADD CONSTRAINT users_user_type_check
//CHECK (user_type IN (
//               'ADMIN',
//    'EMPLOYEE',
//               'CUSTOMER',
//               'VEHICLE_DRIVER',
//               'SUPPLIER',
//               'PARTNER'
//));