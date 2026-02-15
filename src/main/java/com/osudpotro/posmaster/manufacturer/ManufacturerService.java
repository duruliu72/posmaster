package com.osudpotro.posmaster.manufacturer;

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

    public ManufacturerDto createManufacturer(ManufacturerCreateRequest request) {
        if (manufacturerRepository.existsByName(request.getName())) {
            throw new DuplicateManufacturerException();
        }
        var user = authService.getCurrentUser();
        var manufacturer = manufacturerMapper.toEntity(request);
        if(request.getName()!=null){
            String alias=request.getName().replace(" ","-").toLowerCase();
            manufacturer.setAlias(alias);
        }
        manufacturer.setCreatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    public ManufacturerDto updateManufacturer(Long manufacturerId, ManufacturerUpdateRequest request) {
        var manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(ManufacturerNotFoundException::new);
        var user = authService.getCurrentUser();
        manufacturerMapper.update(request, manufacturer);
        if(request.getName()!=null){
            String alias=request.getName().replace(" ","-").toLowerCase();
            manufacturer.setAlias(alias);
        }
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    public ManufacturerDto getManufacturer(Long manufacturerId) {
        var manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(ManufacturerNotFoundException::new);
        return manufacturerMapper.toDto(manufacturer);
    }

    public Manufacturer getManufacturerEntity(Long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
    }

    public ManufacturerDto activeManufacturer(Long manufacturerId) {
        var manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user = authService.getCurrentUser();
        manufacturer.setStatus(1);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    public ManufacturerDto deactivateManufacturer(Long manufacturerId) {
        var manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user = authService.getCurrentUser();
        manufacturer.setStatus(2);
        manufacturer.setUpdatedBy(user);
        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    public ManufacturerDto deleteManufacturer(Long manufacturerId) {
        var manufacturer = manufacturerRepository.findById(manufacturerId).orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found with ID: " + manufacturerId));
        var user = authService.getCurrentUser();
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
            String alias = cols.length > 1 ? cols[1] : null;
            if (name == null) {
                continue; // Skip invalid rows
            }
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            if(name.trim().isEmpty()){
                continue; // Skip invalid rows
            }
            String finalName = name;
            boolean exists = manufacturers.stream()
                    .anyMatch(m -> m.getName().equals(finalName));

            if (exists) {
                continue; // Skip invalid rows
            }
//            Manufacturer foundItem = manufacturers.stream()
//                    .filter(m -> m.getName().equals(finalName))
//                    .findFirst()
//                    .orElse(null);
//            if (foundItem!=null) {
//                continue; // Skip invalid rows
//            }
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setName(name);
            if (alias != null && !alias.trim().isEmpty()) {
                manufacturer.setAlias(alias);
            }
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