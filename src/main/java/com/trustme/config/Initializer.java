package com.trustme.config;

import com.trustme.model.Role;
import com.trustme.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            Role superAdminRole = new Role();
            superAdminRole.setRoles(new String[]{"SUPERADMIN","ADMIN",  "USER"});
            superAdminRole.setId(Long.parseLong("1"));

            Role adminRole = new Role();
            adminRole.setRoles(new String[]{"ADMIN",  "USER"});
            adminRole.setId(Long.parseLong("2"));


            Role userRole = new Role();
            userRole.setRoles(new String[]{"USER"});
            userRole.setId(Long.parseLong("3"));

            roleRepository.saveAll(Arrays.asList(superAdminRole, adminRole, userRole));
            System.out.println("Default roles initialized: SUPERADMIN, ADMIN, USER");
        }
    }
}
