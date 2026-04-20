package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.address.area.AreaRepository;
import com.osudpotro.posmaster.address.city.City;
import com.osudpotro.posmaster.address.city.CityRepository;
import com.osudpotro.posmaster.address.district.District;
import com.osudpotro.posmaster.address.district.DistrictRepository;
import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.address.division.DivisionRepository;
import com.osudpotro.posmaster.address.thana.Thana;
import com.osudpotro.posmaster.address.thana.ThanaRepository;
import com.osudpotro.posmaster.address.upozila.Upozila;
import com.osudpotro.posmaster.address.upozila.UpozilaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasedOnEntityResolverService {
    @Autowired
    private DivisionRepository divisionRepo;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private ThanaRepository thanaRepo;
    @Autowired
    private UpozilaRepository upozilaRepo;
    @Autowired
    private CityRepository cityRepo;
    @Autowired
    private AreaRepository areaRepo;

    public Object getBasedOnEntity(DeliveryCharge deliveryCharge) {
//        if (deliveryCharge.getChargeBasedOn() == null ||
//                deliveryCharge.getBasedOnEntityId() == null) {
//            return null;
//        }
        Integer chargeBasedOn = deliveryCharge.getChargeBasedOn();
//        Long entityId = deliveryCharge.getBasedOnEntityId();
        //    1=For Distance,2=for Area,3=for Division,4=For District,5=For Thana,6=For Upozila,7= For City,8= For State
        switch (chargeBasedOn) {
            case 2:
//                return areaRepo.findById(entityId).orElse(null);
            case 3:
//                return divisionRepo.findById(entityId).orElse(null);
            case 4:
//                return districtRepo.findById(entityId).orElse(null);
            case 5:
//                return thanaRepo.findById(entityId).orElse(null);
            case 6:
//                return upozilaRepo.findById(entityId).orElse(null);
            case 7:
//                return cityRepo.findById(entityId).orElse(null);
//            case 8:
//                return null;
            default:
                return null;
        }
    }
    public String getBasedOnEntityName(DeliveryCharge deliveryCharge) {
        Object entity = getBasedOnEntity(deliveryCharge);
        if (entity == null) return null;
        // Assuming all location entities have a getName() method
        if (entity instanceof Division) return ((Division) entity).getName();
        if (entity instanceof District) return ((District) entity).getName();
        if (entity instanceof Thana) return ((Thana) entity).getName();
        if (entity instanceof Upozila) return ((Upozila) entity).getName();
        if (entity instanceof City) return ((City) entity).getName();
        if (entity instanceof Area) return ((Area) entity).getName();
        return null;
    }
}
