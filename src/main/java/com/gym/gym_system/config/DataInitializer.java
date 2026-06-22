package com.gym.gym_system.config;

import com.gym.gym_system.entity.AppUser;
import com.gym.gym_system.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(AppUserRepository appUserRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            if (appUserRepository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                appUserRepository.save(admin);
            }
            if (appUserRepository.findByUsername("user").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("user");
                admin.setPassword(encoder.encode("user123"));
                admin.setRole("USER");
                appUserRepository.save(admin);
            }
        };
    }
}