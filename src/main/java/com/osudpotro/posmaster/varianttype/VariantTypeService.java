package com.osudpotro.posmaster.varianttype;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VariantTypeService {
    private final AuthService authService;
    private final VariantTypeRepository variantTypeRepository;
    private final VariantTypeMapper variantTypeMapper;
    private final CsvReader csvReader;

    public List<VariantTypeDto> getAllVariantTypes() {
        return variantTypeRepository.findAll()
                .stream()
                .map(variantTypeMapper::toDto)
                .toList();
    }

    public VariantTypeDto createVariantType(VariantTypeCreateRequest request) {
        if (variantTypeRepository.existsByName(request.getName())) {
            throw new DuplicateVariantTypeException();
        }
        var user = authService.getCurrentUser();
        var supplier = variantTypeMapper.toEntity(request);
        supplier.setCreatedBy(user);
        variantTypeRepository.save(supplier);
        return variantTypeMapper.toDto(supplier);
    }

    public VariantTypeDto updateVariantType(Long variantTypeId, VariantTypeUpdateRequest request) {
        var supplier = variantTypeRepository.findById(variantTypeId).orElseThrow(VariantTypeNotFoundException::new);
        var user = authService.getCurrentUser();
        variantTypeMapper.update(request, supplier);
        supplier.setUpdatedBy(user);
        variantTypeRepository.save(supplier);
        return variantTypeMapper.toDto(supplier);
    }

    public VariantTypeDto getVariantType(Long variantTypeId) {
        var supplier = variantTypeRepository.findById(variantTypeId).orElseThrow(() -> new VariantTypeNotFoundException("Variant Type not found with ID: " + variantTypeId));
        return variantTypeMapper.toDto(supplier);
    }

    public VariantType getVariantTypeEntity(Long variantTypeId) {
        return variantTypeRepository.findById(variantTypeId).orElseThrow(() -> new VariantTypeNotFoundException("Variant Type not found with ID: " + variantTypeId));
    }

    public VariantType getVariantTypeEntityOrNull(Long variantTypeId) {
        return variantTypeRepository.findById(variantTypeId).orElseThrow();
    }

    public VariantTypeDto activeVariantType(Long variantTypeId) {
        var supplier = variantTypeRepository.findById(variantTypeId).orElseThrow(() -> new VariantTypeNotFoundException("Variant Type not found with ID: " + variantTypeId));
        var user = authService.getCurrentUser();
        supplier.setStatus(1);
        supplier.setUpdatedBy(user);
        variantTypeRepository.save(supplier);
        return variantTypeMapper.toDto(supplier);
    }

    public VariantTypeDto deActiveVariantType(Long variantTypeId) {
        var supplier = variantTypeRepository.findById(variantTypeId).orElseThrow(() -> new VariantTypeNotFoundException("Variant Type not found with ID: " + variantTypeId));
        var user = authService.getCurrentUser();
        supplier.setStatus(2);
        supplier.setUpdatedBy(user);
        variantTypeRepository.save(supplier);
        return variantTypeMapper.toDto(supplier);
    }

    public VariantTypeDto deleteVariantType(Long variantTypeId) {
        var supplier = variantTypeRepository.findById(variantTypeId).orElseThrow(() -> new VariantTypeNotFoundException("Variant Type not found with ID: " + variantTypeId));
        var user = authService.getCurrentUser();
        supplier.setStatus(3);
        supplier.setUpdatedBy(user);
        variantTypeRepository.save(supplier);
        return variantTypeMapper.toDto(supplier);
    }

    public Page<VariantTypeDto> getVariantTypes(VariantTypeFilter filter, Pageable pageable) {
        return variantTypeRepository.findAll(VariantTypeSpecification.filter(filter), pageable).map(variantTypeMapper::toDto);
    }

    public int importVariantType(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<VariantType> variantTypes = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            VariantType variantType = new VariantType();
            variantType.setName(name.trim());
            variantType.setCreatedBy(user);
            variantTypes.add(variantType);
            count++;
        }
        variantTypeRepository.saveAll(variantTypes);
        return count;
    }

    public int deleteBulkVariantType(List<Long> variantTypeIds) {
        return variantTypeRepository.deleteBulkVariantType(variantTypeIds, 3L);
    }
}