package com.gym.gym_system.repository;

import com.gym.gym_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 🔹 FILTRO POR FECHA
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    // 🔹 INGRESO ENTRE FECHAS
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getIncomeBetween(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    // 🔹 INGRESO DEL MES (este es el que te falta)
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE MONTH(p.paymentDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(p.paymentDate) = YEAR(CURRENT_DATE)")
    BigDecimal getCurrentMonthIncome();
}