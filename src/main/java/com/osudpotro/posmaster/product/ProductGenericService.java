package com.osudpotro.posmaster.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductGenericService {
    @Autowired
    private ProductGenericRepository productGenericRepository;
    boolean existsProductGeneric(Long productId,Long genericId,Long genericUnitId){
        return  productGenericRepository.existsByProductIdAndGenericIdAndGenericUnitId(productId,genericId,genericUnitId);
    }
}