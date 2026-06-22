package com.gym.gym_system.controller;

import com.gym.gym_system.repository.ClientRepository;
import com.gym.gym_system.repository.MembershipRepository;
import com.gym.gym_system.repository.PaymentRepository;
import com.gym.gym_system.service.MembershipService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ClientRepository clientRepository;
    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final MembershipService membershipService;

    public DashboardController(ClientRepository clientRepository,
                               MembershipRepository membershipRepository,
                               PaymentRepository paymentRepository,
                               MembershipService membershipService) {
        this.clientRepository = clientRepository;
        this.membershipRepository = membershipRepository;
        this.paymentRepository = paymentRepository;
        this.membershipService = membershipService;
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {

        membershipService.updateExpiredMemberships();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("totalClients", clientRepository.count());
        model.addAttribute("activeMemberships", membershipRepository.countByStatus("ACTIVE"));
        model.addAttribute("expiredMembershipsCount", membershipRepository.countByStatus("EXPIRED"));
        model.addAttribute("totalPayments", paymentRepository.count());
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            model.addAttribute("monthlyIncome", paymentRepository.getCurrentMonthIncome());
        }

        model.addAttribute("expiredMemberships", membershipService.getExpiredMemberships());
        model.addAttribute("endingToday", membershipService.getMembershipsEndingToday());
        model.addAttribute("endingSoon", membershipService.getMembershipsEndingSoon());

        return "dashboard";
    }
}