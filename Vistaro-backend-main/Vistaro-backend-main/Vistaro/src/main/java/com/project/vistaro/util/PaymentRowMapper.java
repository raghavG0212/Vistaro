package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.Payment;
import com.project.vistaro.model.PaymentMode;
import com.project.vistaro.model.PaymentStatus;

public class PaymentRowMapper implements RowMapper<Payment>{

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Payment p = new Payment();
		p.setPaymentId(rs.getInt("payment_id"));
		p.setBookingId(rs.getInt("booking_id"));
		p.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
		p.setTransactionId(rs.getString("transaction_id"));
		p.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
		Timestamp paid = rs.getTimestamp("paid_at");
		if(paid!=null) p.setPaidAt(paid.toLocalDateTime());
		return p;
	}

}
