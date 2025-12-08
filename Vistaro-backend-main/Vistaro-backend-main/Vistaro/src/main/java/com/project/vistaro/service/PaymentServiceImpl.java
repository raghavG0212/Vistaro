package com.project.vistaro.service;
import org.springframework.stereotype.Service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Payment;
import com.project.vistaro.repository.PaymentDao;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	private PaymentDao paymentDao;
	
	public PaymentServiceImpl(PaymentDao paymentDao) {
		super();
		this.paymentDao = paymentDao;
	}

	@Override
	public Payment save(Payment payment) {
		return paymentDao.save(payment);
	}

	@Override
	public Payment findById(int paymentId) {
		return paymentDao.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payment not found with id : "+paymentId));
	}

}
