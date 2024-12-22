package com.trustme.service;

import com.trustme.constant.ConstantResponses;
import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserEditResponse;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.StatusCode;
import com.trustme.exception.exceptions.AccountNotFoundException;
import com.trustme.mapper.CustomUserMapper;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.mapper.UserMapper;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service class for managing user-related operations.
 * This class contains methods for retrieving user information, updating user details, etc.
 */
@Slf4j
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
    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of UserDto objects representing all users
     */
    public List<UserDto> getUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(CustomUserMapper::getUserDto).collect(Collectors.toList());
    }

    /**
     * Finds a user by their account name.
     *
     * @param accountName the account name of the user
     * @return the User object if found
     * @throws IllegalArgumentException if the input account name is not valid
     * @throws AccountNotFoundException if the user does not exist
     */
    public User findUser(String accountName) throws AccountNotFoundException {
        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException("Account name must not be null or blank");
        }

        return userRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AccountNotFoundException("User with account name '" + accountName + "' not found"));
    }

    /**
     * Update an existing user.
     *
     * @param user target user
     * @param userEditRequest changes to the user
     * */
    public void updateUser(User user, UserEditRequest userEditRequest){
        user.setAccountName(userEditRequest.getAccountName());
        user.setDisabled(userEditRequest.isDisabled());
        try{
            Optional<Role> role = roleRepository.findById(userEditRequest.getRole());
            role.ifPresent(user::setRole);
        } catch (InvalidDataAccessApiUsageException e){
            log.warn("RoleId not found, no changes for role");
        }

    }
    /**
     * Find and update an existing user.
     *
     * @param userEditRequest changes to the user
     * @return a UserEditResponse that contains a UserDto
     * */
    public UserEditResponse editUser(UserEditRequest userEditRequest) {
        try {
            Optional<User> optionalUser = userRepository.findByAccountName(userEditRequest.getTargetAccount());
            if (optionalUser.isEmpty())
                throw new AccountNotFoundException();
            User user = optionalUser.get();
            log.info("get from optional user" +user.getId());
            updateUser(user, userEditRequest);
            log.info("user updated");
            // LazyInitializationException for role
            userRepository.saveAndFlush(user);
            return new UserEditResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), CustomUserMapper.getUserDto(user));
        } catch (AccountNotFoundException | IllegalArgumentException e){
            return ConstantResponses.INVALID_USER_TO_EDIT;
        }

    }
//    public LoginResponse createLoginResponse(User user) {
//        return userMapper.toUserResponse(user);
//    }

}
