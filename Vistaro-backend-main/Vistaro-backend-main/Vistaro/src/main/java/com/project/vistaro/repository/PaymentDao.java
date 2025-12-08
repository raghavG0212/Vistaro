package com.project.vistaro.repository;

import java.util.Optional;

import com.project.vistaro.model.Payment;

public interface PaymentDao {

    Payment save(Payment payment);

    Optional<Payment> findById(int paymentId);

    Optional<Payment> findByBookingId(int bookingId);

    int update(Payment payment);
}
