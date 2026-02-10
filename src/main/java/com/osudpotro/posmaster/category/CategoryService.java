package com.osudpotro.posmaster.category;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.picture.Picture;
import com.osudpotro.posmaster.picture.PictureNotFoundException;
import com.osudpotro.posmaster.picture.PictureRepository;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class CategoryService {
    private final AuthService authService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CustomCategoryMapper customCategoryMapper;
    private final PictureRepository pictureRepository;
    private final MultimediaRepository multimediaRepository;
    private final CsvReader csvReader;

    public List<CategoryDto> gerAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(customCategoryMapper::toDto)
                .toList();
    }

    public Page<CategoryDto> getCategories(CategoryFilter filter, Pageable pageable) {
        return categoryRepository.findAll(CategorySpecification.filter(filter), pageable).map(customCategoryMapper::toDto);
    }

    public int importCategories(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Category> categories = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            String description = cols.length > 1 ? cols[1] : null;
            String imageLink = cols.length > 2 ? cols[2] : null;
            String parentName = cols.length > 3 ? cols[3] : null;
            if (name == null) {
                continue; // Skip invalid rows
            }
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            name = name.replace("- ", ", ");
            if (name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Category findCategory = categoryRepository.findByName(name).orElse(null);
            if (findCategory != null) {
                continue; // Skip invalid rows
            }
            Category category = new Category();
            category.setName(name);
            if(description!=null){
                description = description.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                description = description.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                description = description.replace("- ", ", ");
                category.setDescription(description);
            }
            //For Alias
            String alias = name.replace(" ", "-").toLowerCase();
            category.setAlias(alias);
            if (imageLink != null) {
                imageLink = imageLink.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                imageLink = imageLink.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                imageLink = imageLink.replace("- ", ", ");
                if(!imageLink.isEmpty()){
                    Multimedia multimedia = new Multimedia();
                    multimedia.setName(name);
                    multimedia.setImageUrl(imageLink);
                    multimedia.setLinked(true);
                    multimedia.setSourceLink(2);
                    multimedia.setCreatedBy(user);
                    multimediaRepository.save(multimedia);
                    category.setMedia(multimedia);
                }else {
                    category.setMedia(null);
                }
            }
            category.setCreatedBy(user);
            if (parentName != null) {
                parentName = parentName.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                parentName = parentName.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
                parentName = parentName.replace("- ", ", ");
                Category parentCategory = categoryRepository.findByName(parentName).orElse(null);
                if (parentCategory != null) {
                    category.setParentCat(parentCategory);
                }
            }
            categoryRepository.save(category);
//            categories.add(category);
            count++;
        }
//        categoryRepository.saveAll(categories);
        return count;
    }

    public CategoryDto CreateCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateCategoryException();
        }
        var category = categoryMapper.toEntity(request);
        if (request.getParent_cat_id() != null) {
            var parentCategory = categoryRepository.findById(request.getParent_cat_id()).orElseThrow(ParentCategoryNotFoundException::new);
            category.setParentCat(parentCategory);
        }
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getPictureId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                category.setMedia(multimedia);
            }
        }
        //For Alias
        if (request.getName() != null) {
            String alias = request.getName().replace(" ", "-").toLowerCase();
            category.setAlias(alias);
        }
        var user = authService.getCurrentUser();
        category.setCreatedBy(user);
        categoryRepository.save(category);
        return customCategoryMapper.toDto(category);
    }

    public CategoryDto updateCategory(Long categoryId, CategoryUpdateRequest request) {
        var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if (!Objects.equals(category.getName(), request.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new DuplicateCategoryException();
            }
        }
        Long pictureId = null;
        if (category.getPicture() != null) {
            pictureId = category.getPicture().getId();
        }
        if (request.getPictureId() != null) {
            if (!Objects.equals(pictureId, request.getPictureId())) {
                var findCat = categoryRepository.findByPictureId(request.getPictureId()).orElse(null);
                if (findCat != null && findCat.getPicture().getId().equals(request.getPictureId())) {
                    throw new CategoryImageException();
                }
                Picture picture = pictureRepository.findById(request.getPictureId()).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + request.getPictureId()));
                picture.setLinked(true);
                category.setPicture(picture);
            }
        } else {
            if (category.getPicture() != null) {
                Picture picture = pictureRepository.findById(category.getPicture().getId()).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + category.getPicture().getId()));
                picture.setLinked(false);
                pictureRepository.save(picture);
            }
            category.setPicture(null);
        }

        var user = authService.getCurrentUser();
        categoryMapper.update(request, category);
        if (request.getParent_cat_id() != null) {
            if (!categoryId.equals(request.getParent_cat_id())) {
                var parentCategory = categoryRepository.findById(request.getParent_cat_id()).orElseThrow(ParentCategoryNotFoundException::new);
                category.setParentCat(parentCategory);
            } else {
                throw new CategorySelfParentException();
            }
        } else {
            category.setParentCat(null);
        }
        //For Alias
        if (request.getName() != null) {
            String alias = request.getName().replace(" ", "-").toLowerCase();
            category.setAlias(alias);
        }
        category.setUpdatedBy(user);
        categoryRepository.save(category);
        return customCategoryMapper.toDto(category);
    }

    public CategoryDto getCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if (category.getParentCat() != null) {
            var categoryParent = categoryRepository.findById(category.getParentCat().getId()).orElseThrow(CategoryNotFoundException::new);
            category.setParentCat(categoryParent);
        }
        return customCategoryMapper.toDto(category);
    }

    public CategoryDto getCategoryOrNull(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow();
//        if (category.getParentCat() != null) {
//            var categoryParent = categoryRepository.findById(category.getParentCat().getId()).orElseThrow(CategoryNotFoundException::new);
//            category.setParentCat(categoryParent);
//        }
        return customCategoryMapper.toDto(category);
    }

    public Category getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    public CategoryDto activeCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        var user = authService.getCurrentUser();
        category.setStatus(1);
        category.setUpdatedBy(user);
        categoryRepository.save(category);
        return customCategoryMapper.toDto(category);
    }

    public CategoryDto deactivateCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        var user = authService.getCurrentUser();
        category.setStatus(2);
        category.setUpdatedBy(user);
        categoryRepository.save(category);
        return customCategoryMapper.toDto(category);
    }

    public List<CategoryDto> getChildren(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(customCategoryMapper::toDto)
                .toList();
    }

    public List<Long> getChildrenIds(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(Category::getId)
                .toList();
    }

    public CategoryDto deleteCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        var user = authService.getCurrentUser();
        category.setStatus(3);
        category.setUpdatedBy(user);
        categoryRepository.save(category);
        return customCategoryMapper.toDto(category);
    }

    public int deleteBulkCategory(List<Long> ids) {
        return categoryRepository.deleteBulkCategory(ids, 3L);
    }

    private void loadChildrenRecursively(Category category) {
        List<Category> children = categoryRepository.findByParentId(category.getId());
        category.setChildren(children);
        for (Category child : children) {
            loadChildrenRecursively(child);
        }
    }
}