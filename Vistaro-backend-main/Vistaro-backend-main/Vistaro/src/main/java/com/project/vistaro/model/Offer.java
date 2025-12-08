package com.project.vistaro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Offer {

    private Integer offerId;
    private String code;
    private String description;
    private Integer discountPercent;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validTill;
    private Boolean isActive;

    public Offer() {}

	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Integer discountPercent) {
		this.discountPercent = discountPercent;
	}

	public BigDecimal getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(BigDecimal maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Offer(Integer offerId, String code, String description, Integer discountPercent, BigDecimal maxDiscount,
			LocalDate validFrom, LocalDate validTill, Boolean isActive) {
		super();
		this.offerId = offerId;
		this.code = code;
		this.description = description;
		this.discountPercent = discountPercent;
		this.maxDiscount = maxDiscount;
		this.validFrom = validFrom;
		this.validTill = validTill;
		this.isActive = isActive;
	}

  
}
