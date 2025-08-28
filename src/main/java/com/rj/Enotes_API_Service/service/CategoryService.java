package com.rj.Enotes_API_Service.service;


import java.util.List;

import com.rj.Enotes_API_Service.dto.CategoryDto;
import com.rj.Enotes_API_Service.dto.CategoryResponse;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto>getAllCategory();

    public List<CategoryResponse>getActivCategory();

    public CategoryDto getCategoryById(Integer id) throws Exception;

    public Boolean deleteCategoryById(Integer id);

    
}
