package com.gym.gym_system.service;

import com.gym.gym_system.entity.Payment;
import com.gym.gym_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    public BigDecimal getIncomeBetween(LocalDate startDate, LocalDate endDate){
        return paymentRepository.getIncomeBetween(startDate, endDate);
    }
}