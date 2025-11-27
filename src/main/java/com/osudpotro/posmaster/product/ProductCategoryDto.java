package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryDto;
import lombok.Data;

@Data
public class ProductCategoryDto {
    private ProductDto product;
    private CategoryDto category;
}
