package com.osudpotro.posmaster.genericunit;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.generic.Generic;
import com.osudpotro.posmaster.generic.GenericDto;
import com.osudpotro.posmaster.generic.GenericFilter;
import com.osudpotro.posmaster.generic.GenericSpecification;
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
public class GenericUnitService {
    private final AuthService authService;
    private final GenericUnitRepository genericUnitRepository;
    private final GenericUnitMapper genericUnitMapper;
    private final CsvReader csvReader;

    public List<GenericUnitDto> getAllGenericUnits() {
        return genericUnitRepository.findAll()
                .stream()
                .map(genericUnitMapper::toDto)
                .toList();
    }
    public GenericUnitDto createGenericUnit(GenericUnitCreateRequest request){
        if(genericUnitRepository.existsByName(request.getName())){
            throw new DuplicateGenericUnitException();
        }
        var user = authService.getCurrentUser();
        var genericUnit=genericUnitMapper.toEntity(request);
        genericUnit.setCreatedBy(user);
        genericUnitRepository.save(genericUnit);
        return genericUnitMapper.toDto(genericUnit);
    }
    public GenericUnitDto updateGenericUnit(Long genericUnitId,GenericUnitUpdateRequest request){
        var genericUnit= genericUnitRepository.findById(genericUnitId).orElseThrow(GenericUnitNotFoundException::new);
        var user = authService.getCurrentUser();
        genericUnitMapper.update(request, genericUnit);
        genericUnit.setUpdatedBy(user);
        genericUnitRepository.save(genericUnit);
        return genericUnitMapper.toDto(genericUnit);
    }
    public GenericUnitDto getGenericUnit(Long genericUnitId){
        var genericUnit=genericUnitRepository.findById(genericUnitId).orElseThrow(() -> new GenericUnitNotFoundException("Generic Unit not found with ID: " + genericUnitId));
        return genericUnitMapper.toDto(genericUnit);
    }
    public GenericUnit getGenericUnitEntity(Long genericUnitId){
        return genericUnitRepository.findById(genericUnitId).orElseThrow(() -> new GenericUnitNotFoundException("Generic Unit not found with ID: " + genericUnitId));
    }
    public GenericUnitDto activateGenericUnit(Long genericUnitId){
        var genericUnit = genericUnitRepository.findById(genericUnitId).orElseThrow(() -> new GenericUnitNotFoundException("Generic Unit not found with ID: " + genericUnitId));
        var user = authService.getCurrentUser();
        genericUnit.setStatus(1);
        genericUnit.setUpdatedBy(user);
        genericUnitRepository.save(genericUnit);
        return genericUnitMapper.toDto(genericUnit);
    }
    public GenericUnitDto deactivateGenericUnit(Long genericUnitId){
        var genericUnit=genericUnitRepository.findById(genericUnitId).orElseThrow(() -> new GenericUnitNotFoundException("Generic Unit not found with ID: " + genericUnitId));
        var user=authService.getCurrentUser();
        genericUnit.setStatus(2);
        genericUnit.setUpdatedBy(user);
        genericUnitRepository.save(genericUnit);
        return genericUnitMapper.toDto(genericUnit);
    }
    public GenericUnitDto deleteGenericUnit(Long genericUnitId){
        var genericUnit=genericUnitRepository.findById(genericUnitId).orElseThrow(() -> new GenericUnitNotFoundException("Generic Unit not found with ID: " + genericUnitId));
        var user=authService.getCurrentUser();
        genericUnit.setStatus(3);
        genericUnit.setUpdatedBy(user);
        genericUnitRepository.save(genericUnit);
        return genericUnitMapper.toDto(genericUnit);
    }
    public Page<GenericUnitDto> getGenericUnits(GenericUnitFilter filter, Pageable pageable) {
        return genericUnitRepository.findAll(GenericUnitSpecification.filter(filter), pageable).map(genericUnitMapper::toDto);
    }

    public int importGenericUnit(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<GenericUnit> genericUnits = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            GenericUnit genericUnit = new GenericUnit();
            genericUnit.setName(name.trim());
            genericUnit.setCreatedBy(user);
            genericUnits.add(genericUnit);
            count++;
        }
        genericUnitRepository.saveAll(genericUnits);
        return count;
    }

    public int deleteBulkGenericUnit(List<Long> branchUnitIds) {
        return genericUnitRepository.deleteBulkGenericUnit(branchUnitIds, 3L);
    }
}