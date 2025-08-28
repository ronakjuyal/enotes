package com.rj.Enotes_API_Service.service;

import com.rj.Enotes_API_Service.dto.UserDto;

public interface UserService {

    public Boolean register(UserDto userDto)throws Exception;
    

}
