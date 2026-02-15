package com.osudpotro.posmaster.supplier;

import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SupplierService {
    private final AuthService authService;
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final CsvReader csvReader;

    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    public SupplierDto createSupplier(SupplierCreateRequest request) {
        if (supplierRepository.existsByName(request.getName())) {
            throw new DuplicateSupplierException();
        }
        var user = authService.getCurrentUser();
        var supplier = supplierMapper.toEntity(request);
        supplier.setCreatedBy(user);
        supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto updateSupplier(Long supplierId, SupplierUpdateRequest request) {
        var supplier = supplierRepository.findById(supplierId).orElseThrow(SupplierNotFoundException::new);
        var user = authService.getCurrentUser();
        supplierMapper.update(request, supplier);
        supplier.setUpdatedBy(user);
        supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto getSupplier(Long supplierId) {
        var supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier Unit not found with ID: " + supplierId));
        return supplierMapper.toDto(supplier);
    }

    public Supplier getSupplierEntity(Long supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier Unit not found with ID: " + supplierId));
    }

    public SupplierDto activeSupplier(Long supplierId) {
        var supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier Unit not found with ID: " + supplierId));
        var user = authService.getCurrentUser();
        supplier.setStatus(1);
        supplier.setUpdatedBy(user);
        supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto deactiveSupplier(Long supplierId) {
        var supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier Unit not found with ID: " + supplierId));
        var user = authService.getCurrentUser();
        supplier.setStatus(2);
        supplier.setUpdatedBy(user);
        supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto deleteSupplier(Long supplierId) {
        var supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier Unit not found with ID: " + supplierId));
        var user = authService.getCurrentUser();
        supplier.setStatus(3);
        supplier.setUpdatedBy(user);
        supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    public Page<SupplierDto> getSuppliers(SupplierFilter filter, Pageable pageable) {
        return supplierRepository.findAll(SupplierSpecification.filter(filter), pageable).map(supplierMapper::toDto);
    }

    public int importSupplier(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Supplier> suppliers = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Supplier supplier = new Supplier();
            supplier.setName(name.trim());
            supplier.setCreatedBy(user);
            suppliers.add(supplier);
            count++;
        }
        supplierRepository.saveAll(suppliers);
        return count;
    }

    public int deleteBulkSupplier(List<Long> organizationIds) {
        return supplierRepository.deleteBulkSupplier(organizationIds, 3L);
    }
}