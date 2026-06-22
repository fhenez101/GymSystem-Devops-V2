package com.gym.gym_system.repository;

import com.gym.gym_system.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}