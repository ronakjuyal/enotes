package com.rj.Enotes_API_Service.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rj.Enotes_API_Service.dto.CategoryDto;
import com.rj.Enotes_API_Service.dto.CategoryResponse;
import com.rj.Enotes_API_Service.entity.Category;
import com.rj.Enotes_API_Service.exception.ResourceNotFoundException;
import com.rj.Enotes_API_Service.service.CategoryService;
import com.rj.Enotes_API_Service.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?>saveCategory(@RequestBody  CategoryDto categoryDto ){
        Boolean saveCategory =categoryService.saveCategory(categoryDto);
        if(saveCategory){

           return  CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
        }else{
            return CommonUtil.createErrorResponseMessage("category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
    }
    
    @GetMapping("/")
    public ResponseEntity<?>getAllCategory(){
      
        List<CategoryDto>allCategories=categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategories)){
            return ResponseEntity.noContent().build();
        }else{
            return CommonUtil.createBuildResponse(allCategories, HttpStatus.OK);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?>getActiveCategory(){
        List<CategoryResponse>allCategories=categoryService.getActivCategory();
        if(CollectionUtils.isEmpty(allCategories)){
            return ResponseEntity.noContent().build();
        }else{
            return CommonUtil.createBuildResponse(allCategories, HttpStatus.OK);
           // return new ResponseEntity<>(allCategories,HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getCategoryDetailsById(@PathVariable Integer id)throws Exception{
        CategoryDto categoryDto= categoryService.getCategoryById(id); 
            if (ObjectUtils.isEmpty(categoryDto)) {
                return CommonUtil.createErrorResponseMessage("Internal server error", HttpStatus.NOT_FOUND);

               // return new ResponseEntity<>("Internal server error ",HttpStatus.NOT_FOUND);
            }
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
          //  return new ResponseEntity<>(categoryDto,HttpStatus.OK);
        }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteCategoryById(@PathVariable Integer id){
        Boolean deleted =categoryService.deleteCategoryById(id);
        if (deleted) {
            return CommonUtil.createBuildResponse("Category deleted successfully", HttpStatus.OK);

           // return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);

       // return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
