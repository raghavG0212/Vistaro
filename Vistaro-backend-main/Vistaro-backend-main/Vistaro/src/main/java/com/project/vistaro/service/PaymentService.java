package com.project.vistaro.service;

import com.project.vistaro.model.Payment;

public interface PaymentService {
	
	Payment save(Payment payment);
    Payment findById(int paymentId);
}
