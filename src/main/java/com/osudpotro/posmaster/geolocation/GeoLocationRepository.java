package com.osudpotro.posmaster.geolocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GeoLocationRepository extends JpaSpecificationExecutor<GeoLocation>, JpaRepository<GeoLocation, Long> {
    Optional<GeoLocation> findByLatitudeAndLongitude(Double latitude, Double longitude);
    //    @Query("SELECT g FROM GeoLocation g WHERE " +
//            "REPLACE(g.locationName, '-', ' ') LIKE %:searchKey% OR " +
//            "REPLACE(g.locationName, ' ', '-') LIKE %:searchKey% OR " +
//            "g.locationName LIKE %:searchKey%")
//    List<GeoLocation> searchByLocationName(@Param("searchKey") String searchKey);
    @Query(value = "SELECT * FROM geo_locations g WHERE " +
            "REPLACE(g.location_name, '-', ' ') LIKE %:searchKey% OR " +
            "REPLACE(g.location_name, ' ', '-') LIKE %:searchKey% OR " +
            "g.location_name LIKE %:searchKey% limit 5", nativeQuery = true)
    List<GeoLocation> searchByLocationName(@Param("searchKey") String searchKey);
}
