package com.trustme.utils;

import com.trustme.enums.Roles;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthUtils {
    public static List<String> getJwtAuthoritiesFromRoles(UserDetails user){
        List<Roles> roles = new ArrayList<>();
        user.getAuthorities().stream().forEach(role -> {
            roles.add(Roles.fromString(role.getAuthority()));
        });
        Set<String> authorities = new HashSet<>();
        roles.stream().forEach(role->authorities.addAll(role.getAuthorities()));
        List<String> authoritiesList = authorities.stream().toList();
        return authoritiesList;
    }
}
