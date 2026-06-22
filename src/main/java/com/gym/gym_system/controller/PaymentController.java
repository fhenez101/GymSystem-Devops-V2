package com.gym.gym_system.controller;

import com.gym.gym_system.entity.Client;
import com.gym.gym_system.entity.Payment;
import com.gym.gym_system.service.ClientService;
import com.gym.gym_system.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ClientService clientService;

    public PaymentController(PaymentService paymentService, ClientService clientService) {
        this.paymentService = paymentService;
        this.clientService = clientService;
    }

    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.findAll());
        model.addAttribute("payment", new Payment());
        model.addAttribute("clients", clientService.findAll());
        return "payments/list";
    }

    @PostMapping
    public String savePayment(@ModelAttribute Payment payment,
                              @RequestParam("clientId") Long clientId) {

        Client client = clientService.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clientId));

        payment.setClient(client);
        paymentService.save(payment);

        return "redirect:/payments";
    }

    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
        return "redirect:/payments";
    }
}