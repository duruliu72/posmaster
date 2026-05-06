package com.osudpotro.posmaster.deliverymethod;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DeliveryMethodService {
    @Autowired
    private AuthService authService;
    @Autowired
    private DeliveryMethodRepository dvmRepo;
    @Autowired
    private MultimediaRepository multimediaRepository;
    @Autowired
    private DeliveryMethodMapper dvmMapper;

    public List<DeliveryMethodDto> getAllEntities() {
        return dvmRepo.findAll()
                .stream()
                .map(dvmMapper::toDto)
                .toList();
    }

    public Page<DeliveryMethodDto> getAllEntities(DeliveryMethodFilter filter, Pageable pageable) {
        return dvmRepo.findAll(DeliveryMethodSpecification.filter(filter), pageable).map(dvmMapper::toDto);
    }

    public DeliveryMethodDto getEntity(Long entityId) {
        var entity = dvmRepo.findById(entityId).orElseThrow(() -> new DeliveryMethodNotFoundException("Delivery Method not found with ID: " + entityId));
        return dvmMapper.toDto(entity);
    }

    public DeliveryMethodDto createEntity(DeliveryMethodCreateRequest request) {
        if (dvmRepo.existsByTitle(request.getTitle())) {
            throw new DuplicateDeliveryMethodException();
        }
        var user = authService.getCurrentUser();
        DeliveryMethod dvm = new DeliveryMethod();
        dvm.setTitle(request.getTitle());
        dvm.setMessage(request.getMessage());
        dvm.setDefaultDeliveryFee(request.getDefaultDeliveryFee());
        dvm.setDefaultMinSaleAmountForDeliveryFree(request.getDefaultMinSaleAmountForDeliveryFree());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.parse(request.getFromDate(), formatter);
            dvm.setFromDate(fromDate);
        }
        if (request.getToDate() != null) {
            LocalDateTime toDate = LocalDateTime.parse(request.getToDate(), formatter);
            dvm.setToDate(toDate);
        }
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                dvm.setMedia(multimedia);
            }
        }
        dvm.setCreatedBy(user);
        dvmRepo.save(dvm);
        return dvmMapper.toDto(dvm);
    }

    public DeliveryMethodDto updateEntity(Long entityId, DeliveryMethodUpdateRequest request) {
        DeliveryMethod dvm = dvmRepo.findById(entityId).orElseThrow(DeliveryMethodNotFoundException::new);
        var user = authService.getCurrentUser();
        dvm.setTitle(request.getTitle());
        dvm.setMessage(request.getMessage());
        dvm.setDefaultDeliveryFee(request.getDefaultDeliveryFee());
        dvm.setDefaultMinSaleAmountForDeliveryFree(request.getDefaultMinSaleAmountForDeliveryFree());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.parse(request.getFromDate(), formatter);
            dvm.setFromDate(fromDate);
        }
        if (request.getToDate() != null) {
            LocalDateTime toDate = LocalDateTime.parse(request.getToDate(), formatter);
            dvm.setToDate(toDate);
        }
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                dvm.setMedia(multimedia);
            }
        }
        dvm.setUpdatedBy(user);
        dvmRepo.save(dvm);
        return dvmMapper.toDto(dvm);
    }

    public DeliveryMethodDto deleteEntity(Long entityId) {
        var entity = dvmRepo.findById(entityId).orElseThrow(() -> new DeliveryMethodNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        entity.setStatus(3);
        entity.setUpdatedBy(user);
        dvmRepo.save(entity);
        return dvmMapper.toDto(entity);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return dvmRepo.deleteBulkEntity(entityIds, 3L);
    }

    public DeliveryMethodDto activateEntity(Long entityId) {
        var area = dvmRepo.findById(entityId).orElseThrow(() -> new DeliveryMethodNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        area.setStatus(1);
        area.setUpdatedBy(user);
        dvmRepo.save(area);
        return dvmMapper.toDto(area);
    }

    public DeliveryMethodDto deactivateEntity(Long entityId) {
        var branch = dvmRepo.findById(entityId).orElseThrow(() -> new DeliveryMethodNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        branch.setStatus(2);
        branch.setUpdatedBy(user);
        dvmRepo.save(branch);
        return dvmMapper.toDto(branch);
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
