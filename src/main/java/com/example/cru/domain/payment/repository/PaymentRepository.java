package com.example.cru.domain.payment.repository;

import com.example.cru.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Long, Payment> {
}
