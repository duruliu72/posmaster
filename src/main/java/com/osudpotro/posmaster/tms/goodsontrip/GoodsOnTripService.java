package com.osudpotro.posmaster.tms.goodsontrip;

import com.osudpotro.posmaster.tms.vehicletrip.TripStatus;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GoodsOnTripService {
    private final AuthService authService;
    private final GoodsOnTripRepository goodsOnTripRepository;
    private final GoodsOnTripMapper goodsOnTripMapper;

    @Transactional
    public GoodsOnTripDto updateGoodsTrip(Long goodsOnTripId, UpdateGoodsOnTripRequest request) {
        GoodsOnTrip gooodsOnTrip = goodsOnTripRepository.findById(goodsOnTripId).orElseThrow(() -> new GoodsOnTripNotFoundException("Goods on Trip not found with ID: " + goodsOnTripId));
        //        if(gooodsOnTrip.)
        var authUser = authService.getCurrentUser();
        GoodsStatus goodsStatus=gooodsOnTrip.getGoodsStatus();
        if (goodsStatus.equals(GoodsStatus.ASSIGN_TO_VEHICLE)) {
            gooodsOnTrip.setGoodsStatus(GoodsStatus.LOADED);
            gooodsOnTrip.setLoadedBy(authUser);
        }
        if (goodsStatus.equals(GoodsStatus.LOADED)) {
            gooodsOnTrip.setGoodsStatus(GoodsStatus.DELIVERED);
            gooodsOnTrip.setUnLoadedBy(authUser);
        }
        gooodsOnTrip.setRemarks(request.getRemarks());

        VehicleTrip vehicleTrip = gooodsOnTrip.getVehicleTrip();
        if (vehicleTrip != null) {
            if (vehicleTrip.getGoodsOnTrips().size() == vehicleTrip.getTotalDelivered()) {
                vehicleTrip.setTripStatus(TripStatus.COMPLETED);
            }
            if (vehicleTrip.getTripStatus() == TripStatus.SCHEDULED) {
                vehicleTrip.setTripStatus(TripStatus.IN_PROGRESS);
            }
        }
        goodsOnTripRepository.save(gooodsOnTrip);
        return goodsOnTripMapper.toDto(gooodsOnTrip);
    }
}
