package com.gym.gym_system.repository;

import com.gym.gym_system.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByEndDateBeforeAndStatus(LocalDate date, String status);

    List<Membership> findByStatus(String status);

    List<Membership> findByEndDate(LocalDate date);

    List<Membership> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    long countByStatus(String status);

    List<Membership> findByEndDateAndStatusAndWhatsappSentFalse(LocalDate date, String status);
}