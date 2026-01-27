package com.osudpotro.posmaster.generic;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.branch.BranchDto;
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
public class GenericService {
    private final AuthService authService;
    private final GenericRepository genericRepository;
    private final GenericMapper genericMapper;
    private final CsvReader csvReader;

    public List<GenericDto> gerAllGenerics() {
        return genericRepository.findAll()
                .stream()
                .map(genericMapper::toDto)
                .toList();
    }
    public GenericDto createGeneric(GenericCreateRequest request){
        if(genericRepository.existsByName(request.getName())){
            throw new DuplicateGenericException();
        }
        var user=authService.getCurrentUser();
        var generic=genericMapper.toEntity(request);
        generic.setCreatedBy(user);
        genericRepository.save(generic);
        return genericMapper.toDto(generic);
    }
    public GenericDto updateGeneric(Long genericId, GenericUpdateRequest request){
        var generic= genericRepository.findById(genericId).orElseThrow(GenericNotFoundException::new);
        var user = authService.getCurrentUser();
        genericMapper.update(request, generic);
        generic.setUpdatedBy(user);
        genericRepository.save(generic);
        return genericMapper.toDto(generic);
    }
    public GenericDto getGeneric(Long genericId){
        var generic= genericRepository.findById(genericId).orElseThrow(GenericNotFoundException::new);
        return genericMapper.toDto(generic);
    }
    public Generic getGenericEntity(Long genericId){
        return genericRepository.findById(genericId).orElseThrow(GenericNotFoundException::new);
    }
    public GenericDto activeGeneric(Long genericId){
        var generic=genericRepository.findById(genericId).orElseThrow(() -> new GenericNotFoundException("Generic not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(1);
        generic.setUpdatedBy(user);
        genericRepository.save(generic);
        return genericMapper.toDto(generic);
    }
    public GenericDto deactivateGeneric(Long genericId){
        var generic=genericRepository.findById(genericId).orElseThrow(() -> new GenericNotFoundException("Generic not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(2);
        generic.setUpdatedBy(user);
        genericRepository.save(generic);
        return genericMapper.toDto(generic);
    }
    public GenericDto deleteGeneric(Long genericId){
        var generic=genericRepository.findById(genericId).orElseThrow(() -> new GenericNotFoundException("Generic not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(3);
        generic.setUpdatedBy(user);
        genericRepository.save(generic);
        return genericMapper.toDto(generic);
    }
    public Page<GenericDto> getGenerics(GenericFilter filter, Pageable pageable) {
        return genericRepository.findAll(GenericSpecification.filter(filter), pageable).map(genericMapper::toDto);
    }

    public int importGeneric(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Generic> generics = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null) {
                continue; // Skip invalid rows
            }
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            if(name.trim().isEmpty()){
                continue; // Skip invalid rows
            }
            Generic generic = new Generic();
            generic.setName(name.trim());
            generic.setCreatedBy(user);
            generics.add(generic);
            count++;
        }
        genericRepository.saveAll(generics);
        return count;
    }

    public int deleteBulkGeneric(List<Long> genericIds) {
        return genericRepository.deleteBulkGeneric(genericIds, 3L);
    }
}