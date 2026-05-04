package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.purchase.*;
import com.osudpotro.posmaster.salecart.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private AddressMapper addressMapper;

    public List<AddressDto> getAllEntities() {
        return addressRepo.findAll()
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    public Page<AddressDto> getAllEntities(AddressFilter filter, Pageable pageable) {
        return addressRepo.findAll(AddressSpecification.filter(filter), pageable).map(addressMapper::toDto);
    }

    public AddressDto getEntity(Long entityId) {
        var entity = addressRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Sale Cart not found with ID: " + entityId));
        return addressMapper.toDto(entity);
    }

    public AddressDto createEntity(SaleCartCreateRequest request) {
        var authUser = authService.getCurrentUser();
        Address address = new Address();
        address.setEmail(request.getEmail());
        address.setMobile(request.getMobile());
        return addressMapper.toDto(address);
    }

    public AddressDto updateEntity(Long saleCartId, UpdateSaleCartRequest request) {
        var address = addressRepo.findById(saleCartId).orElseThrow(PurchaseException::new);
        var authUser = authService.getCurrentUser();
        address.setEmail(request.getEmail());
        address.setMobile(request.getMobile());
        address.setUpdatedBy(authUser);
        return addressMapper.toDto(address);
    }

    public AddressDto deleteEntity(Long entityId) {
        Address address = addressRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        address.setStatus(3);
        address.setUpdatedBy(user);
        addressRepo.save(address);
        return addressMapper.toDto(address);
    }
}
