package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PurchaseService {
    @Autowired
    private AuthService authService;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    PurchaseMapper purchaseMapper;

    public Page<PurchaseDto> getAllEntities(PurchaseFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return purchaseRepo.findAll(PurchaseSpecification.filter(filter,authUser), pageable).map(purchaseMapper::toDto);
    }
    public PurchaseDto getEntity(Long entityId) {
        var entity = purchaseRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Area not found with ID: " + entityId));
        return purchaseMapper.toMaxDto(entity);
    }
}
