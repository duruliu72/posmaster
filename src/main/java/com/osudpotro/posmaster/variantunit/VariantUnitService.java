package com.osudpotro.posmaster.variantunit;

import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.utility.CsvReader;
import com.osudpotro.posmaster.varianttype.VariantType;
import com.osudpotro.posmaster.varianttype.VariantTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class VariantUnitService {
    private final AuthService authService;
    private final VariantUnitRepository variantUnitRepository;
    private final VariantTypeService variantTypeService;
    private final VariantUnitMapper variantUnitMapper;
    private final CustomVariantUnitMapper customVariantUnitMapper;
    private final CsvReader csvReader;
    public List<VariantUnitDto> gerAllVariantUnits() {
        return variantUnitRepository.findAll()
                .stream()
                .map(customVariantUnitMapper::toDto)
                .toList();
    }
    public VariantUnitDto createVariantUnit(VariantUnitCreateRequest request) {
        if(variantUnitRepository.existsByName(request.getName())){
            throw new DuplicateVariantUnitException();
        }
        var user=authService.getCurrentUser();
        var variantUnit= variantUnitMapper.toEntity(request);
        VariantType variantType=variantTypeService.getVariantTypeEntity(request.getVariantTypeId());
        variantUnit.setVariantType(variantType);
        variantUnit.setCreatedBy(user);

        variantUnitRepository.save(variantUnit);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public VariantUnitDto updateVariantUnit(Long variantUnitId, VariantUnitUpdateRequest request) {
        var variantUnit= variantUnitRepository.findById(variantUnitId).orElseThrow(VariantUnitNotFoundException::new);
        var user = authService.getCurrentUser();
        variantUnitMapper.update(request, variantUnit);
        VariantType variantType=variantTypeService.getVariantTypeEntity(request.getVariantTypeId());
        variantUnit.setVariantType(variantType);
        variantUnit.setUpdatedBy(user);
        variantUnitRepository.save(variantUnit);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public VariantUnitDto getVariantUnit(Long variantUnitId) {
        var variantUnit= variantUnitRepository.findById(variantUnitId).orElseThrow(VariantUnitNotFoundException::new);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public VariantUnit getVariantUnitEntity(Long variantUnitId) {
        return variantUnitRepository.findById(variantUnitId).orElseThrow(VariantUnitNotFoundException::new);
    }

    public VariantUnitDto deleteVariantUnit(Long variantUnitId) {
        var variantUnit= variantUnitRepository.findById(variantUnitId).orElseThrow(() -> new VariantUnitNotFoundException("Variant Unit not found with ID: " + variantUnitId));
        var user=authService.getCurrentUser();
        variantUnit.setStatus(3);
        variantUnit.setUpdatedBy(user);
        variantUnitRepository.save(variantUnit);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public VariantUnitDto activateVariantUnit(Long variantUnitId) {
        var variantUnit= variantUnitRepository.findById(variantUnitId).orElseThrow(() -> new VariantUnitNotFoundException("Variant Unit not found with ID: " + variantUnitId));
        var user=authService.getCurrentUser();
        variantUnit.setStatus(1);
        variantUnit.setUpdatedBy(user);
        variantUnitRepository.save(variantUnit);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public VariantUnitDto deactivateVariantUnit(Long variantUnitId) {
        var variantUnit= variantUnitRepository.findById(variantUnitId).orElseThrow(() -> new VariantUnitNotFoundException("Variant Unit not found with ID: " + variantUnitId));
        var user=authService.getCurrentUser();
        variantUnit.setStatus(2);
        variantUnit.setUpdatedBy(user);
        variantUnitRepository.save(variantUnit);
        return customVariantUnitMapper.toDto(variantUnit);
    }
    public Page<VariantUnitDto> getVariantUnits(VariantUnitFilter filter, Pageable pageable) {
        return variantUnitRepository.findAll(VariantUnitSpecification.filter(filter), pageable).map(customVariantUnitMapper::toDto);
    }
    public int importVariantUnit(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<VariantUnit> variantUnits = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            String variantTypeIdString = cols.length > 1 ? cols[1] : null;

            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Long variantTypeId = null;
           VariantType variantType = null;
            if(variantTypeIdString!=null&&!variantTypeIdString.trim().isEmpty()){
                try {
                    variantTypeId = Long.parseLong(cols[1].trim());
                    variantType=variantTypeService.getVariantTypeEntityOrNull(variantTypeId);
                } catch (NumberFormatException e) {
                    // Handle invalid number
                    System.out.println("Invalid variantTypeId: " + cols[1]);
                }
            }
            VariantUnit variantUnit = new VariantUnit();
            variantUnit.setName(name.trim());
            if(variantType!=null){
                variantUnit.setVariantType(variantType);
            }
            variantUnit.setCreatedBy(user);
            variantUnits.add(variantUnit);
            count++;
        }
        variantUnitRepository.saveAll(variantUnits);
        return count;
    }
    public int deleteBulkVariantUnit(List<Long> productTypeIds) {
        return variantUnitRepository.deleteBulkVariantUnit(productTypeIds, 3L);
    }
}