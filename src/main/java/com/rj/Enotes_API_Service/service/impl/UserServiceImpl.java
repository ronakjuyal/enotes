package com.rj.Enotes_API_Service.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rj.Enotes_API_Service.dto.EmailRequest;
import com.rj.Enotes_API_Service.dto.UserDto;
import com.rj.Enotes_API_Service.entity.Role;
import com.rj.Enotes_API_Service.entity.User;
import com.rj.Enotes_API_Service.repository.RoleRepository;
import com.rj.Enotes_API_Service.repository.UserRepository;
import com.rj.Enotes_API_Service.service.EmailService;
import com.rj.Enotes_API_Service.service.UserService;
import com.rj.Enotes_API_Service.util.Validation;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean register(UserDto userDto) throws Exception {

        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);

        setRole(userDto, user);

        User saveUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(saveUser)) {

            sendEmail(saveUser);
            return true;
        }
        return false;
    }

    private void sendEmail(User saveUser) throws Exception {

        String message = "Hi, <b>" + saveUser.getFirstName()
                + "</b><br>Your account registration is successful.<br>click the below link to verify your account<br> <a href='#'>click here</a>";
        EmailRequest emailRequest = EmailRequest.builder()
                .to(saveUser.getEmail())
                .title("Account registraton")
                .subject("Account created successfully")
                .message(message)
                .build();

            emailService.send(emailRequest);

            
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();

        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
