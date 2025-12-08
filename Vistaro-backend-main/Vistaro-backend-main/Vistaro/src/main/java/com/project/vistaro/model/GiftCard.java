package com.project.vistaro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GiftCard {

    private Integer giftcardId;
    private String code;
    private BigDecimal amount;
    private Boolean isRedeemed;
    private LocalDate expiryDate;

    public GiftCard() {}

	public Integer getGiftcardId() {
		return giftcardId;
	}

	public void setGiftcardId(Integer giftcardId) {
		this.giftcardId = giftcardId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getIsRedeemed() {
		return isRedeemed;
	}

	public void setIsRedeemed(Boolean isRedeemed) {
		this.isRedeemed = isRedeemed;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public GiftCard(Integer giftcardId, String code, BigDecimal amount, Boolean isRedeemed, LocalDate expiryDate) {
		super();
		this.giftcardId = giftcardId;
		this.code = code;
		this.amount = amount;
		this.isRedeemed = isRedeemed;
		this.expiryDate = expiryDate;
	}

    
}
