package com.project.vistaro.service;

import com.project.vistaro.model.Offer;
import com.project.vistaro.repository.OfferDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferDao offerDao;

    public OfferServiceImpl(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    @Override
    public Map<String, Object> validateOffer(String code, double amount) {

        Offer offer = offerDao.findByCode(code)
                .orElse(null);

        Map<String, Object> res = new HashMap<>();

        if (offer == null) {
            res.put("valid", false);
            res.put("reason", "Invalid code");
            return res;
        }

        LocalDate today = LocalDate.now();

        if (!offer.getIsActive() ||
                today.isBefore(offer.getValidFrom()) ||
                today.isAfter(offer.getValidTill())) {

            res.put("valid", false);
            res.put("reason", "Offer expired or inactive");
            return res;
        }

        double discount = (offer.getDiscountPercent() / 100.0) * amount;
        discount = Math.min(discount, offer.getMaxDiscount().doubleValue());

        res.put("valid", true);
        res.put("discount", discount);
        res.put("finalAmount", amount - discount);
        return res;
    }
}
