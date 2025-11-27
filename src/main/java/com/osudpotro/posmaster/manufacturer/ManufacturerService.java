package com.osudpotro.posmaster.manufacturer;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.genericunit.GenericUnit;
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
public class ManufacturerService {
    private final AuthService authService;
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;
    private final CsvReader csvReader;

    public List<ManufacturerDto> gerAllManufacturers() {
        return manufacturerRepository.findAll()
                .stream()
                .map(manufacturerMapper::toDto)
                .toList();
    }
    public ManufacturerDto createManufacturer(ManufacturerCreateRequest request){
        if(manufacturerRepository.existsByName(request.getName())){
            throw new DuplicateManufacturerException();
        }
        var user=authService.getCurrentUser();
        var manufacturer=manufacturerMapper.toEntity(request);
        manufacturer.setCreatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
    public ManufacturerDto updateManufacturer(Long manufacturerId, ManufacturerUpdateRequest request){
        var manufacturer= manufacturerRepository.findById(manufacturerId).orElseThrow(ManufacturerNotFoundException::new);
        var user = authService.getCurrentUser();
        manufacturerMapper.update(request, manufacturer);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
    public ManufacturerDto getManufacturer(Long manufacturerId){
        var manufacturer= manufacturerRepository.findById(manufacturerId).orElseThrow(ManufacturerNotFoundException::new);
        return manufacturerMapper.toDto(manufacturer);
    }
    public Manufacturer getManufacturerEntity(Long manufacturerId){
        return manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
    }
    public ManufacturerDto activeManufacturer(Long manufacturerId){
        var manufacturer=manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user=authService.getCurrentUser();
        manufacturer.setStatus(1);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
    public ManufacturerDto deactivateManufacturer(Long manufacturerId){
        var manufacturer=manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user=authService.getCurrentUser();
        manufacturer.setStatus(2);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
    public ManufacturerDto deleteManufacturer(Long manufacturerId){
        var manufacturer=manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user=authService.getCurrentUser();
        manufacturer.setStatus(3);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
    public Page<ManufacturerDto> getManufacturers(ManufacturerFilter filter, Pageable pageable) {
        return manufacturerRepository.findAll(ManufacturerSpecification.filter(filter), pageable).map(manufacturerMapper::toDto);
    }

    public int importManufacturer(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Manufacturer> manufacturers = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setName(name.trim());
            manufacturer.setCreatedBy(user);
            manufacturers.add(manufacturer);
            count++;
        }
        manufacturerRepository.saveAll(manufacturers);
        return count;
    }

    public int deleteBulkManufacturer(List<Long> manufacturerIds) {
        return manufacturerRepository.deleteBulkManufacturer(manufacturerIds, 3L);
    }
}