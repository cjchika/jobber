package com.cjchika.jobber.category.service;

import com.cjchika.jobber.category.dto.CategoryRequestDTO;
import com.cjchika.jobber.category.dto.CategoryResponseDTO;
import com.cjchika.jobber.category.dto.CategoryUpdateDTO;
import com.cjchika.jobber.category.mapper.CategoryMapper;
import com.cjchika.jobber.category.model.Category;
import com.cjchika.jobber.category.repository.CategoryRepository;
import com.cjchika.jobber.exception.JobberException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO){
        if(categoryRepository.existsByName(categoryRequestDTO.getName())){
            throw new JobberException("Category with this name already exists", HttpStatus.CONFLICT);
        }

        Category category = CategoryMapper.toModel(categoryRequestDTO);
        category = categoryRepository.save(category);
        return CategoryMapper.toDTO(category);
    }

    public List<CategoryResponseDTO> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new JobberException("Category not found", HttpStatus.NOT_FOUND));

        return CategoryMapper.toDTO(category);
    }

    public CategoryResponseDTO updateCategory(UUID id, CategoryUpdateDTO categoryUpdateDTO){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new JobberException("Category not found", HttpStatus.NOT_FOUND));

        if (categoryUpdateDTO.getName() != null &&
                !categoryUpdateDTO.getName().equals(category.getName()) &&
                categoryRepository.existsByName(categoryUpdateDTO.getName())) {
            throw new JobberException("Category with this name already exists", HttpStatus.CONFLICT);
        }

        CategoryMapper.updateModel(categoryUpdateDTO, category);
        category = categoryRepository.save(category);
        return CategoryMapper.toDTO(category);
    }

    public void deleteCategory(UUID id){
        if(!categoryRepository.existsById(id)){
            throw new JobberException("Category not found", HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}
