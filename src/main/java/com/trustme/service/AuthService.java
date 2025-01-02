package com.trustme.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.enums.Roles;
import com.trustme.enums.StatusCode;
import com.trustme.exception.exceptions.ResourceNotAvailableException;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import com.trustme.dto.mapper.CustomUserMapper;
import com.trustme.model.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
/**
 * Service class for handling authentication-related and jwt encoding operations.
 * This class contains methods for generating signing keys and jwt, user registration, login, and other authentication functionalities.
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    String ENCODED_SIGNING_KEY_BASE64 = "BynaRVN1R8shGkku6SmSnQJzGc8ZSs7aTQzDRlnD2ckNfZ+EDEInq0ap6Ktqcm6meg3sNQaLyDGOCRw6eMC1Vg==";

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the system.
     *
     * @param registerRequest the request object containing user registration details
     */
    public UserResponse registerUser(UserRegisterRequest registerRequest) {
        if (!userRepository.findByUsername(registerRequest.getUsername()).isEmpty()){
            throw new ResourceNotAvailableException("User existed!");
        }
        Long roleId = 1L;
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new ResourceNotFoundException("Role not found" + roleId));
        System.out.println(registerRequest.toString());
        User user = User.builder()
                .username(registerRequest.getUsername())
                .accountName(registerRequest.getAccountName())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .balance(1000.0)
                .createdAt(LocalDateTime.now())
                .pinCode(registerRequest.getPinCode())
                .role(role)
                .build();
        userRepository.save(user);
        return new UserResponse(StatusCode.CREATED.getHttpStatus(),StatusCode.CREATED.getStatusMessage(), CustomUserMapper.getUserDto(user));
    }


    public LoginResponse loginUser(UserLoginRequest loginRequest){
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
            List<String> authoritiesList = getJwtAuthoritiesFromRoles(userDetails);
            User user = userDetails.getUser();
            System.out.println(user.getUsername() + user.getPassword());
            String token = generateJwt(user.getUsername(), authoritiesList, 3600L);
        return new LoginResponse(200, "Login successful", token);
    }

    /**
     * Retrieve the current logged in userDto
     *
     * @return UserDto of the current user
     */
    public UserDto getCurrentUserDto(){
        return CustomUserMapper.getUserDto(getCurrentUser());
    }

    /**
     * Retrieve the current logged in user
     *
     * @return current User
     */
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return userRepository.findByUsername(jwt.getSubject())
                .orElseThrow(()-> new ResourceNotAvailableException("Login required!"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
    /**
     * Extract authorities from user's role
     * @param user authenticated user
     * @return a list of authorities (scopes) for jwt
     * */
    public List<String> getJwtAuthoritiesFromRoles(UserDetails user) {
        List<Roles> roles = new ArrayList<>();
        user.getAuthorities().stream().forEach(role -> {
            roles.add(Roles.fromString(role.getAuthority()));
        });
        Set<String> authorities = new HashSet<>();
        roles.stream().forEach(role -> authorities.addAll(role.getAuthorities()));
        List<String> authoritiesList = authorities.stream().toList();
        return authoritiesList;
    }


    public String generateSigningKey() {
        byte[] key = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        String signingKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated Signing Key (Base64): " + signingKey);
        return signingKey;
    }
    /**
     * Generate a jwt based on the given username, scopes, and time.
     * @param username name of the authenticated user
     * @param scopes authorities according to user's roles
     * @param timeMillis life duration of the token
     * @return a jwt string that is then sent back to the client
     * */
    public String generateJwt(String username, List<String> scopes, Long timeMillis) {

        byte[] key = Base64.getDecoder().decode(ENCODED_SIGNING_KEY_BASE64);

        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuer("https://localhost:8080/auth")
                .setAudience("https://localhost:8080/api")
                .claim("scope", scopes)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeMillis * 1000))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwt;
    }

}
