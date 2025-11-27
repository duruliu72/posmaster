package com.osudpotro.posmaster.brand;

import com.osudpotro.posmaster.auth.AuthService;
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
public class BrandService {
    private final AuthService authService;
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final CsvReader csvReader;

    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toDto)
                .toList();
    }

    public Page<BrandDto> getBrands(BrandFilter filter, Pageable pageable) {
        return brandRepository.findAll(BrandSpecification.filter(filter), pageable).map(brandMapper::toDto);
    }

    public int importBrands(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Brand> brands = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Brand brand = new Brand();
            brand.setName(name.trim());
            brand.setCreatedBy(user);
            brands.add(brand);
            count++;
        }
        brandRepository.saveAll(brands);
        return count;
    }

    public BrandDto createBrand(BrandCreateRequest request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateBrandException();
        }
        var user = authService.getCurrentUser();
        var resource = brandMapper.toEntity(request);
        resource.setCreatedBy(user);
        brandRepository.save(resource);
        return brandMapper.toDto(resource);
    }

    public BrandDto updateBrand(Long brandId, BrandUpdateRequest request) {
        var brand = brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
        var user = authService.getCurrentUser();
        brandMapper.update(request, brand);
        brand.setUpdatedBy(user);
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    public BrandDto getBrand(Long brandId) {
        var brand = brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
        return brandMapper.toDto(brand);
    }

    public Brand getBrandEntity(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
    }

    public BrandDto activeBrand(Long brandId) {
        var brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException("Brand not found with ID: " + brandId));
        var user = authService.getCurrentUser();
        brand.setStatus(1);
        brand.setUpdatedBy(user);
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    public BrandDto deactivateBrand(Long brandId) {
        var brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException("Brand not found with ID: " + brandId));
        var user = authService.getCurrentUser();
        brand.setStatus(2);
        brand.setUpdatedBy(user);
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    public BrandDto deleteBrand(Long brandId) {
        var brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException("Brand not found with ID: " + brandId));
        var user = authService.getCurrentUser();
        brand.setStatus(3);
        brand.setUpdatedBy(user);
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }
    public int deleteBulkBrand(List<Long> ids) {
       return brandRepository.deleteBulkBrand(ids, 3L);
    }
}