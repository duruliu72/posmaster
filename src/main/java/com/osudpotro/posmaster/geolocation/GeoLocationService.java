package com.osudpotro.posmaster.geolocation;

import com.google.maps.*;
import com.google.maps.model.*;
import com.osudpotro.posmaster.config.UtilityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoLocationService {
    @Autowired
    private UtilityConfig utilityConfig;
    @Autowired
    private GeoLocationRepository geoLocationRepository;

    public GeoLocation getLocationFromLatLng(double latitude, double longitude) {
        GeoApiContext context = null;
        try {
            context = new GeoApiContext.Builder()
                    .apiKey(utilityConfig.getGoogleApiKey())
                    .build();
            LatLng latLng = new LatLng(latitude, longitude);
            GeocodingApiRequest request = GeocodingApi.reverseGeocode(context, latLng)
                    .language("en");
            GeocodingResult[] results = request.await();
            if (results != null && results.length > 0) {
                GeocodingResult firstResult = results[0];
                GeoLocation location = new GeoLocation();
                location.setLocationName(firstResult.formattedAddress);
                location.setPlaceId(firstResult.placeId);
                if (firstResult.geometry != null && firstResult.geometry.location != null) {
                    location.setLatitude(firstResult.geometry.location.lat);
                    location.setLongitude(firstResult.geometry.location.lng);
                }
                return location;
            }
        } catch (Exception e) {

        }

        return null;
    }

    public List<GeoLocation> autocomplete(String searchKey) {
        List<GeoLocation> result = new ArrayList<>();
//        First Search From Own Server
        if (searchKey != null && !searchKey.isEmpty()) {
            var list = geoLocationRepository.searchByLocationName(searchKey);
            if (!list.isEmpty()) {
                result = list;
            } else {
                //        2nd Search from Google api
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey(utilityConfig.getGoogleApiKey())
                        .build();
                try {
                    PlaceAutocompleteRequest request = PlacesApi.placeAutocomplete(context, searchKey, null);
                    AutocompletePrediction[] predictions = request.await();
                    for (AutocompletePrediction p : predictions) {
                        PlaceDetailsRequest requestDetails = PlacesApi.placeDetails(
                                context,
                                p.placeId
                        ).fields(
                                PlaceDetailsRequest.FieldMask.ADDRESS_COMPONENT,
                                PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS,
                                PlaceDetailsRequest.FieldMask.GEOMETRY,        // Contains lat/lng!
                                PlaceDetailsRequest.FieldMask.NAME,
                                PlaceDetailsRequest.FieldMask.PLACE_ID,
                                PlaceDetailsRequest.FieldMask.PLUS_CODE,
                                PlaceDetailsRequest.FieldMask.RATING,
                                PlaceDetailsRequest.FieldMask.USER_RATINGS_TOTAL,
                                PlaceDetailsRequest.FieldMask.OPENING_HOURS,
                                PlaceDetailsRequest.FieldMask.PHOTOS,
                                PlaceDetailsRequest.FieldMask.FORMATTED_PHONE_NUMBER,
                                PlaceDetailsRequest.FieldMask.WEBSITE,
                                PlaceDetailsRequest.FieldMask.BUSINESS_STATUS
                        );
                        try {
                            var placeDetails = requestDetails.await();
                            Double lat = placeDetails.geometry.location.lat;
                            Double lng = placeDetails.geometry.location.lng;
                            GeoLocation findGeoLocation = geoLocationRepository.findByLatitudeAndLongitude(lat, lng).orElse(null);
                            if (findGeoLocation == null) {
                                GeoLocation location = new GeoLocation();
                                location.setLocationName(p.description);
                                location.setPlaceId(p.placeId);
                                location.setLatitude(lat);
                                location.setLongitude(lng);
                                if (isLocationInBangladesh(placeDetails)) {
                                    result.add(location);
                                }
                            } else {
                                result.add(findGeoLocation);
                            }
                        } catch (Exception e) {
                            throw new Error();
                        }
                    }
                    geoLocationRepository.saveAll(result);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private boolean isLocationInBangladesh(PlaceDetails placeDetails) {
        if (placeDetails == null || placeDetails.addressComponents == null) {
            return false;
        }
        for (AddressComponent component : placeDetails.addressComponents) {
            for (AddressComponentType type : component.types) {
                // Check if this component is a country
                if (type == AddressComponentType.COUNTRY) {
                    // Check if the country name or short name is Bangladesh
                    boolean isBangladesh = "Bangladesh".equalsIgnoreCase(component.longName) ||
                            "BD".equalsIgnoreCase(component.shortName);
                    if (isBangladesh) {
                        System.out.println("Found Bangladesh location: " +
                                component.longName + " (" + component.shortName + ")");
                        return true;
                    } else {
                        System.out.println("Found other country: " +
                                component.longName + " (" + component.shortName + ")");
                        return false;
                    }
                }
            }
        }
        // If no country component found, transfer formatted address as fallback
        if (placeDetails.formattedAddress != null) {
            String address = placeDetails.formattedAddress.toLowerCase();
            boolean isBangladesh = address.contains("bangladesh") ||
                    address.contains("bd,") ||
                    address.endsWith(", bd") ||
                    address.endsWith(",bd");

            if (isBangladesh) {
                System.out.println("Found Bangladesh location from formatted address");
                return true;
            }
        }

        return false;
    }
}
