package com.trustme.service;

import java.security.SecureRandom;
import java.util.*;

import com.trustme.constant.ConstantResponses;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.enums.Roles;
import com.trustme.enums.StatusCode;
import com.trustme.mapper.CustomUserMapper;
import com.trustme.model.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
     * @throws UserAlreadyExistsException if the user already exists
     */

    /**
     * @comment_by: toanlemanh
     * this method should be provided by UserService
     * does not make use of Mapper
     * t nghi minh ko can tao repo cho role vi minh gan role vao user luon
     * khong co user thi role cung ko ton tai duoc
     *
     *  cung cap gen token o auth service thi hop ly hon
     *
     */
    public UserResponse registerUser(UserRegisterRequest registerRequest) {
        // User user = UserMapper.INSTANCE.toUser (registerRequest);
        Optional<Role> role = roleRepository.findById(1L);
        System.out.println(registerRequest.toString());
        User user = User.builder()
                .username(registerRequest.getUsername())
                .accountName(registerRequest.getAccountName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .balance(1000.0)
                .pinCode(registerRequest.getPinCode())
                .role(role.get())
                .build();
        userRepository.save(user);
        return new UserResponse(StatusCode.CREATED.getHttpStatus(),StatusCode.CREATED.getStatusMessage(), CustomUserMapper.getUserDto(user));
    }


    public LoginResponse loginUser(UserLoginRequest loginRequest){
        try {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            if (userDetails != null) {
                List<String> authoritiesList = getJwtAuthoritiesFromRoles(userDetails);
                User user = userDetails.getUser();
                System.out.println(user.getUsername() + user.getPassword());
                String token = generateJwt(user.getUsername(), authoritiesList, 3600L);
                return new LoginResponse(200, "Login successful", token);
            }
        } catch (BadCredentialsException e) {
            return ConstantResponses.INVALID_CREDENTIALS;
        } catch (UsernameNotFoundException e) {
            return ConstantResponses.USER_NOT_FOUND;
        }
        return ConstantResponses.INVALID_CREDENTIALS;
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
