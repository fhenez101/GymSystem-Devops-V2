package com.gym.gym_system.controller;

import com.gym.gym_system.entity.Client;
import com.gym.gym_system.entity.Membership;
import com.gym.gym_system.service.ClientService;
import com.gym.gym_system.service.MembershipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;
    private final ClientService clientService;

    public MembershipController(MembershipService membershipService, ClientService clientService) {
        this.membershipService = membershipService;
        this.clientService = clientService;
    }

    @GetMapping("/list")
    public String list(Model model) {

        membershipService.updateExpiredMemberships();

        model.addAttribute("memberships", membershipService.findAll());
        model.addAttribute("membership", new Membership());
        model.addAttribute("clients", clientService.findAll());

        model.addAttribute("expiredMemberships", membershipService.getExpiredMemberships());
        model.addAttribute("endingToday", membershipService.getMembershipsEndingToday());
        model.addAttribute("endingSoon", membershipService.getMembershipsEndingSoon());

        return "memberships/list";
    }

    @PostMapping
    public String saveMembership(@ModelAttribute Membership membership,
                                 @RequestParam("clientId") Long clientId) {

        Client client = clientService.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));

        membership.setClient(client);
        membershipService.save(membership);

        return "redirect:/memberships/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMembership(@PathVariable Long id) {
        membershipService.deleteById(id);
        return "redirect:/memberships/list";
    }
}