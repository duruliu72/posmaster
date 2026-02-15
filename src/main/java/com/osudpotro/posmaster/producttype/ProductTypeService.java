package com.osudpotro.posmaster.producttype;

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
public class ProductTypeService {
    private final AuthService authService;
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;
    private final CsvReader csvReader;
    public List<ProductTypeDto> gerAllProductTypes() {
        return productTypeRepository.findAll()
                .stream()
                .map(productTypeMapper::toDto)
                .toList();
    }
    public ProductTypeDto createProductType(ProductTypeCreateRequest request) {
        if(productTypeRepository.existsByName(request.getName())){
            throw new DuplicateProductTypeException();
        }
        var user=authService.getCurrentUser();
        var productType= productTypeMapper.toEntity(request);
        productType.setCreatedBy(user);
        productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }
    public ProductTypeDto updateProductType(Long productTypeId, ProductTypeUpdateRequest request) {
        var productType= productTypeRepository.findById(productTypeId).orElseThrow(ProductTypeNotFoundException::new);
        var user = authService.getCurrentUser();
        productTypeMapper.update(request, productType);
        productType.setUpdatedBy(user);
        productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }
    public ProductTypeDto getProductType(Long productTypeId) {
        var productType= productTypeRepository.findById(productTypeId).orElseThrow(ProductTypeNotFoundException::new);
        return productTypeMapper.toDto(productType);
    }
    public ProductType getProductTypeEntity(Long productTypeId) {
        return productTypeRepository.findById(productTypeId).orElseThrow(ProductTypeNotFoundException::new);
    }

    public ProductTypeDto deleteProductType(Long productTypeId) {
        var productType= productTypeRepository.findById(productTypeId).orElseThrow(() -> new ProductTypeNotFoundException("Product Type not found with ID: " + productTypeId));
        var user=authService.getCurrentUser();
        productType.setStatus(3);
        productType.setUpdatedBy(user);
        productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }
    public ProductTypeDto activateProductType(Long productTypeId) {
        var productType= productTypeRepository.findById(productTypeId).orElseThrow(() -> new ProductTypeNotFoundException("Product Type not found with ID: " + productTypeId));
        var user=authService.getCurrentUser();
        productType.setStatus(1);
        productType.setUpdatedBy(user);
        productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }
    public ProductTypeDto deactivateProductType(Long productTypeId) {
        var productType= productTypeRepository.findById(productTypeId).orElseThrow(() -> new ProductTypeNotFoundException("Product Type not found with ID: " + productTypeId));
        var user=authService.getCurrentUser();
        productType.setStatus(2);
        productType.setUpdatedBy(user);
        productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }
    public Page<ProductTypeDto> getProductTypes(ProductTypeFilter filter, Pageable pageable) {
        return productTypeRepository.findAll(ProductTypeSpecification.filter(filter), pageable).map(productTypeMapper::toDto);
    }
    public int importProductType(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<ProductType> productTypes = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            ProductType productType = new ProductType();
            productType.setName(name.trim());
            productType.setCreatedBy(user);
            productTypes.add(productType);
            count++;
        }
        productTypeRepository.saveAll(productTypes);
        return count;
    }
    public int deleteBulkProductType(List<Long> productTypeIds) {
        return productTypeRepository.deleteBulkProductType(productTypeIds, 3L);
    }

}