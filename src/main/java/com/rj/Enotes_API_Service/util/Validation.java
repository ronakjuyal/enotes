package com.rj.Enotes_API_Service.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.rj.Enotes_API_Service.dto.CategoryDto;
import com.rj.Enotes_API_Service.dto.TodoDto;
import com.rj.Enotes_API_Service.dto.UserDto;
import com.rj.Enotes_API_Service.dto.TodoDto.StatusDto;
import com.rj.Enotes_API_Service.entity.Role;
import com.rj.Enotes_API_Service.enums.TodoStatus;
import com.rj.Enotes_API_Service.exception.ExistDataException;
import com.rj.Enotes_API_Service.exception.ResourceNotFoundException;
import com.rj.Enotes_API_Service.exception.ValidationException;
import com.rj.Enotes_API_Service.repository.RoleRepository;
import com.rj.Enotes_API_Service.repository.UserRepository;

@Component
public class Validation {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public void categoryValidation(CategoryDto categoryDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("category object should not be null or empty");
        } else {

            /////////// validation name field//////////////////////
            if (ObjectUtils.isEmpty(categoryDto.getName())) {
                error.put("name", "name field is empty or null");
            } else {
                if (categoryDto.getName().length() < 3) {
                    error.put("name", "name length minimum 3");
                }
                if (categoryDto.getName().length() > 100) {
                    error.put("name", "name length max 100");
                }
            }
            /////////// validation description field//////////////////////
            if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
                error.put("description", "description field is empty or null");
            }
            /////////// validation is active field//////////////////////
            if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive", "isActive field is empty or null");
            } else {
                if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
                        && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
                    error.put("isActive", "invalid value isActive Field");
                }
            }
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }

    }

    public void todoValidation(TodoDto todo) throws Exception {

        StatusDto reqStatus = todo.getStatus();
        Boolean statusFound = false;

        for (TodoStatus st : TodoStatus.values()) {
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
            }
        }
        if (!statusFound) {
            throw new ResourceNotFoundException("Invalid status");
        }
    }

    public void userValidation(UserDto userDto) throws Exception{

        if (!StringUtils.hasText(userDto.getFirstName())) {

            throw new IllegalArgumentException("First name is invalid");
        }


        if (!StringUtils.hasText(userDto.getLastName())) {

            throw new IllegalArgumentException("Last name is invalid");
        }


        if (!StringUtils.hasText(userDto.getEmail())) {

            throw new IllegalArgumentException("Email is invalid");
        }
        else{
            Boolean existEmail=userRepository.existsByEmail(userDto.getEmail());
            if (existEmail) {
                throw new ExistDataException("Email id already exist");
            }
        }


        if (!StringUtils.hasText(userDto.getMobNo())|| !userDto.getMobNo().matches(Constants.MOBNO_REGEX)) {

            throw new IllegalArgumentException("Mobile number is invalid");
        }


        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("Role is invalid");
        }else{

            List<Integer>roleIds=roleRepository.findAll().stream().map(r->r.getId()).toList();

            List<Integer> invalidReqRoleIds = userDto.getRoles().stream().map(r->r.getId()).filter(roleId->!roleIds.contains(roleId)).toList();

            if (!CollectionUtils.isEmpty(invalidReqRoleIds)) {
                throw new IllegalArgumentException("Role  is invalid "+invalidReqRoleIds);

            }
        }

        
    }
}
