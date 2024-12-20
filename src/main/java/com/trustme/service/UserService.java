package com.trustme.service;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserEditResponse;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.StatusCode;
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
/**
 * Service class for managing user-related operations.
 * This class contains methods for retrieving user information, updating user details, etc.
 */
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

        return users.stream().map(user -> getUserDto(user)).collect(Collectors.toList());
    }

    /**
     * Map an instance of User to UserDto
     * @return UserDto
     */
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
    /**
     * Finds a user by their account name.
     *
     * @param accountName the account name of the user
     * @return the User object if found
     * @throws UserNotFoundException if the user does not exist
     */
    public User findUser(String accountName){
        return userRepository.findByAccountName(accountName).get();
    }
    /**
     * Update an existing user.
     *
     * @param user target user
     * @param userEditRequest changes to the user
     * */
    public void updateUser(User user, UserEditRequest userEditRequest){
        user.setAccountName(userEditRequest.getAccountName());
        Optional<Role> role = roleRepository.findById(userEditRequest.getRole());
        role.ifPresent(user::setRole);
        user.setDisabled(userEditRequest.isDisabled());
    }

    public UserEditResponse editUser(UserEditRequest userEditRequest) {
        Optional<User> optionalUser = userRepository.findByAccountName(userEditRequest.getTargetAccount());
        if (optionalUser.isEmpty())
            return new UserEditResponse(ErrorCode.INVALID_USER.getHttpStatus(), ErrorCode.INVALID_USER.getErrorMessage(), null);
        User user = optionalUser.get();
        updateUser(user, userEditRequest);
        userRepository.save(user);
        return new UserEditResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), user.getAccountName());
    }
//    public LoginResponse createLoginResponse(User user) {
//        return userMapper.toUserResponse(user);
//    }

}
