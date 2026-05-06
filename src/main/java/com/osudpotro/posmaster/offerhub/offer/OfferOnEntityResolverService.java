package com.osudpotro.posmaster.offerhub.offer;

import com.osudpotro.posmaster.address.area.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferOnEntityResolverService {
    @Autowired
    private AreaRepository areaRepo;

    public Object getBasedOnEntity(Offer offer) {
//        if (deliveryCharge.getChargeBasedOn() == null ||
//                deliveryCharge.getBasedOnEntityId() == null) {
//            return null;
//        }
//        Integer offerOn = offerhub.getOfferOn();
        Integer offerOn=1;
//        Long entityId = deliveryCharge.getBasedOnEntityId();
        //    1=For Distance,2=for Area,3=for Division,4=For District,5=For Thana,6=For Upozila,7= For City,8= For State
        switch (offerOn) {
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
    public String getBasedOnEntityName(Offer offer) {
        Object entity = getBasedOnEntity(offer);
//        if (entity == null) return null;
//        // Assuming all location entities have a getName() method
//        if (entity instanceof Division) return ((Division) entity).getName();
//        if (entity instanceof District) return ((District) entity).getName();
//        if (entity instanceof Thana) return ((Thana) entity).getName();
//        if (entity instanceof Upozila) return ((Upozila) entity).getName();
//        if (entity instanceof City) return ((City) entity).getName();
//        if (entity instanceof Area) return ((Area) entity).getName();
        return null;
    }
}
