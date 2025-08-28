package com.rj.Enotes_API_Service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobNo;
    private String password;

    private List<RoleDto> roles;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RoleDto {

        private Integer id;
        private String name;
    }
}
