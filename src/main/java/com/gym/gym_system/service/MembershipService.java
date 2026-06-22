package com.gym.gym_system.service;

import com.gym.gym_system.entity.Membership;
import com.gym.gym_system.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public Membership save(Membership membership) {
        return membershipRepository.save(membership);
    }

    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    public Optional<Membership> findById(Long id) {
        return membershipRepository.findById(id);
    }

    public void deleteById(Long id) {
        membershipRepository.deleteById(id);
    }

    public void updateExpiredMemberships() {

        List<Membership> expiredMemberships = membershipRepository.findByEndDateBeforeAndStatus(LocalDate.now(), "ACTIVE");

        for (Membership membership : expiredMemberships) {
            membership.setStatus("EXPIRED");
            membershipRepository.save(membership);
        }
    }

    public List<Membership> getExpiredMemberships() {
        return membershipRepository.findByStatus("EXPIRED");
    }

    public List<Membership> getMembershipsEndingToday() {
        return membershipRepository.findByEndDate(LocalDate.now());
    }

    public List<Membership> getMembershipsEndingSoon() {
        return membershipRepository.findByEndDateBetween(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
    }
}