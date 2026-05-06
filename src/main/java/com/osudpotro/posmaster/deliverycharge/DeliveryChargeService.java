package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.address.area.AreaNotFoundException;
import com.osudpotro.posmaster.address.area.AreaRepository;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethodNotFoundException;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethodRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DeliveryChargeService {
    @Autowired
    private AuthService authService;
    @Autowired
    private DeliveryChargeRepository dvcRepo;
    @Autowired
    private DeliveryChargeMapper dvcMapper;
    @Autowired
    private DeliveryMethodRepository dvmRepo;
    @Autowired
    private AreaRepository areaRepo;

    public List<DeliveryChargeDto> getAllEntities() {
        return dvcRepo.findAll()
                .stream()
                .map(dvcMapper::toDto)
                .toList();
    }

    public Page<DeliveryChargeDto> getAllEntities(DeliveryChargeFilter filter, Pageable pageable) {
        return dvcRepo.findAll(DeliveryChargeSpecification.filter(filter), pageable).map(dvcMapper::toDto);
    }

    public DeliveryChargeDto getEntity(Long entityId) {
        var entity = dvcRepo.findById(entityId).orElseThrow(() -> new DeliveryChargeNotFoundException("Delivery Charge not found with ID: " + entityId));
        return dvcMapper.toDto(entity);
    }

    public DeliveryChargeDto createEntity(DeliveryChargeCreateRequest request) {
        DeliveryMethod deliveryMethod = dvmRepo.findById(request.getDeliveryMethodId()).orElseThrow(() -> new DeliveryMethodNotFoundException("Delivery Method not found with ID: " + request.getDeliveryMethodId()));
        if (dvcRepo.existsByDeliveryMethodAndIsActive(deliveryMethod, true)) {
            throw new DuplicateDeliveryChargeException();
        }
        var user = authService.getCurrentUser();
        DeliveryCharge dvc = new DeliveryCharge();
        dvc.setDeliveryMethod(deliveryMethod);
        dvc.setDeliveryFee(request.getDeliveryFee());
        dvc.setMinSaleAmountForDeliveryFree(request.getMinSaleAmountForDeliveryFree());
        dvc.setChargeBasedOn(request.getChargeBasedOn());
        dvc.setMinDistance(request.getMinDistance());
        if (request.getAreaId() != null) {
            Area area = areaRepo.findById(request.getAreaId()).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + request.getAreaId()));
            dvc.setArea(area);
        }
        dvc.setIsFree(request.getIsFree());
        dvc.setCreatedBy(user);
        dvcRepo.save(dvc);
        return dvcMapper.toDto(dvc);
    }

    public DeliveryChargeDto updateEntity(Long entityId, DeliveryChargeUpdateRequest request) {
        DeliveryCharge dvc = dvcRepo.findById(entityId).orElseThrow(DeliveryChargeNotFoundException::new);
        var user = authService.getCurrentUser();
        dvc.setDeliveryFee(request.getDeliveryFee());
        dvc.setMinSaleAmountForDeliveryFree(request.getMinSaleAmountForDeliveryFree());
        dvc.setChargeBasedOn(request.getChargeBasedOn());
        dvc.setMinDistance(request.getMinDistance());
        if (request.getAreaId() != null) {
            Area area = areaRepo.findById(request.getAreaId()).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + request.getAreaId()));
            dvc.setArea(area);
        }
        dvc.setIsFree(request.getIsFree());
        dvc.setUpdatedBy(user);
        dvcRepo.save(dvc);
        return dvcMapper.toDto(dvc);
    }

    public DeliveryChargeDto deleteEntity(Long entityId) {
        var entity = dvcRepo.findById(entityId).orElseThrow(() -> new DeliveryChargeNotFoundException("Delivery Charge not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        entity.setStatus(3);
        entity.setUpdatedBy(user);
        dvcRepo.save(entity);
        return dvcMapper.toDto(entity);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return dvcRepo.deleteBulkEntity(entityIds, 3L);
    }

    public DeliveryChargeDto activateEntity(Long entityId) {
        var entity = dvcRepo.findById(entityId).orElseThrow(() -> new DeliveryChargeNotFoundException("Delivery Charge not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        entity.setStatus(1);
        entity.setUpdatedBy(user);
        dvcRepo.save(entity);
        return dvcMapper.toDto(entity);
    }

    public DeliveryChargeDto deactivateEntity(Long entityId) {
        var branch = dvcRepo.findById(entityId).orElseThrow(() -> new DeliveryChargeNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        branch.setStatus(2);
        branch.setUpdatedBy(user);
        dvcRepo.save(branch);
        return dvcMapper.toDto(branch);
    }

    public int importEntities(MultipartFile file) {
        //        var user = authService.getCurrentUser();
//        List<String[]> rows = csvReader.readCSV(file);
//        boolean hasHeader = true;
//        int count = 0;
//        List<Branch> branches = new ArrayList<>();
//        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
//            String[] cols = rows.get(i);
//            // Expecting: name, description
//            String name = cols.length > 0 ? cols[0] : null;
//            if (name == null || name.trim().isEmpty()) {
//                continue; // Skip invalid rows
//            }
//            Branch branch = new Branch();
//            branch.setName(name.trim());
//            branch.setCreatedBy(user);
//            branches.add(branch);
//            count++;
//        }
//        branchRepository.saveAll(branches);
        return 0;
    }
}
