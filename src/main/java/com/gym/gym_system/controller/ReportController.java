package com.gym.gym_system.controller;

import com.gym.gym_system.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final PaymentService paymentService;

    public ReportController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public String paymentsReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        LocalDate start = (startDate == null || startDate.isBlank())
                ? LocalDate.now().withDayOfMonth(1)
                : LocalDate.parse(startDate);

        LocalDate end = (endDate == null || endDate.isBlank())
                ? LocalDate.now()
                : LocalDate.parse(endDate);

        model.addAttribute("payments", paymentService.findByDateRange(start, end));
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("incomeBetween", paymentService.getIncomeBetween(start, end));

        return "reports/payments";
    }
}