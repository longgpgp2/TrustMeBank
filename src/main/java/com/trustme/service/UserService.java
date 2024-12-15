package com.trustme.service;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.mapper.UserMapper;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

//    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDto> getUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> getUserDto(user)).collect(Collectors.toList());
    }

    public UserDto getUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getAccountName(),
                user.getBalance(),
                user.getRole().getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDisabled()
        );
    }

    public User findUser(String accountName){
        return userRepository.findByAccountName(accountName).get();
    }

    public void updateUser(User user, UserEditRequest userEditRequest){
        user.setAccountName(userEditRequest.getAccountName());
        Optional<Role> role = roleRepository.findById(userEditRequest.getRole());
        role.ifPresent(user::setRole);
        user.setDisabled(userEditRequest.isDisabled());
    }
//    public LoginResponse createLoginResponse(User user) {
//        return userMapper.toUserResponse(user);
//    }

}
