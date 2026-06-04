package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.address.area.AreaRepository;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private AreaRepository areaRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private AddressMapper addressMapper;

    public List<AddressDto> getAllEntities() {
        return addressRepo.findAllByOrderByIdAsc()
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

    public AddressDto createEntity(AddressCreateRequest request) {
        var authUser = authService.getCurrentUser();
        Address address = addressMapper.toEntity(request);
        if (request.getCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getCustomerId()).orElse(null);
            address.setCustomer(customer);
            if (customer != null) {
                address.setUser(customer.getUser());
            }
        }
        if (request.getAreaId() != null) {
            Area area = areaRepo.findById(request.getAreaId()).orElse(null);
            address.setArea(area);
        }
        address.setCreatedBy(authUser);
        Address isDefaultAddress = addressRepo.findByCustomerAndIsDefault(address.getCustomer(), true).orElse(null);
        if (isDefaultAddress != null) {
            isDefaultAddress.setIsDefault(false);
            addressRepo.save(isDefaultAddress);
        }
        addressRepo.save(address);
        return addressMapper.toDto(address);
    }

    public AddressDto updateEntity(Long addressId, AddressUpdateRequest request) {
        Address address = addressRepo.findById(addressId).orElseThrow(EntityNotFoundException::new);
        var authUser = authService.getCurrentUser();
        addressMapper.update(request, address);
        Address isDefaultAddress = addressRepo.findByCustomerAndIsDefault(address.getCustomer(),true).orElse(null);
        if (isDefaultAddress != null && !Objects.equals(address.getId(), isDefaultAddress.getId())) {
            isDefaultAddress.setIsDefault(false);
            addressRepo.save(isDefaultAddress);
        }
        address.setUpdatedBy(authUser);
        addressRepo.save(address);
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
