package com.trustme.config;

import java.util.Arrays;

import com.trustme.service.SavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.repository.RoleRepository;
import com.trustme.service.AuthService;
/**
 * Spring Component to initialize Roles and Users (For testing purpose)
 * */
@Component
public class Initializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final SavingService savingService;
    public Initializer(RoleRepository roleRepository, AuthService authService, SavingService savingService) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.savingService = savingService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            Role superAdminRole = new Role();
            superAdminRole.setRoles(new String[] { "SUPERADMIN", "ADMIN", "USER" });
            superAdminRole.setId(Long.parseLong("1"));

            Role adminRole = new Role();
            adminRole.setRoles(new String[] { "ADMIN", "USER" });
            adminRole.setId(Long.parseLong("2"));

            Role userRole = new Role();
            userRole.setRoles(new String[] { "USER" });
            userRole.setId(Long.parseLong("3"));

            roleRepository.saveAll(Arrays.asList(superAdminRole, adminRole, userRole));
            UserRegisterRequest user1 = new UserRegisterRequest("user1", "user1", "a@a", "0123456789", "123456", "123456");
            UserRegisterRequest user2 = new UserRegisterRequest("user2", "user2", "b@b", "0987654321", "123456", "123456");
            authService.registerUser(user1);
            authService.registerUser(user2);

        }
    }
}
