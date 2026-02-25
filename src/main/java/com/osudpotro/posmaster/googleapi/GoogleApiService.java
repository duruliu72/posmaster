package com.osudpotro.posmaster.googleapi;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.AutocompletePrediction;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.config.UtilityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleApiService {
    @Autowired
    private UtilityConfig utilityConfig;
   public List<PlacePrediction> autocomplete(String searchKey){
           GeoApiContext context = new GeoApiContext.Builder()
                   .apiKey(utilityConfig.getGoogleApiKey())
                   .build();
           try {
               PlaceAutocompleteRequest request = PlacesApi.placeAutocomplete(context, searchKey,null);
               AutocompletePrediction[] predictions = request.await();
               return Arrays.stream(predictions)
                       .map(p -> {
                          PlacePrediction pp= new PlacePrediction();
                           pp.setDescription(p.description);
                           pp.setPlaceId(p.placeId);
                           pp.setMainText(p.structuredFormatting != null ?
                                   p.structuredFormatting.mainText : null);
                          try {
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
                              var placeDetails = requestDetails.await();
                              pp.setLatitude(placeDetails.geometry.location.lat);
                              pp.setLongitude(placeDetails.geometry.location.lng);
                              Location northeast=new Location();
                              northeast.setLatitude(placeDetails.geometry.viewport.northeast.lat);
                              northeast.setLongitude(placeDetails.geometry.viewport.northeast.lng);
                              pp.setNortheast(northeast);
                              Location southwest=new Location();
                              southwest.setLatitude(placeDetails.geometry.viewport.southwest.lat);
                              southwest.setLongitude(placeDetails.geometry.viewport.southwest.lng);
                              pp.setSouthwest(southwest);
                          }catch (Exception e){

                          }
                          return  pp;
                       })
                       .collect(Collectors.toList());
           } catch (Exception e) {
               return  new ArrayList<>();
           }

   }
}
